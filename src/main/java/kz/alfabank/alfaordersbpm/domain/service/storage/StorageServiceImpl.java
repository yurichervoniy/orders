package kz.alfabank.alfaordersbpm.domain.service.storage;

import kz.alfabank.alfaordersbpm.config.AlfrescoProperties;
import kz.alfabank.alfaordersbpm.domain.models.attachment.Attachment;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.dto.StorageContent;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.AlfrescoFileException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.AlfrescoOrder;
import kz.alfabank.alfaordersbpm.domain.repositories.AttachmentRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import kz.alfabank.utils.alfresco.AlfrescoConfig;
import kz.alfabank.utils.alfresco.AlfrescoExplorer;
import kz.alfabank.utils.alfresco.AlfrescoFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Transactional(timeout = 45, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class StorageServiceImpl implements StorageService{

    private static final Map<String, String> FILE_EXTENSIONS = new HashMap(16);

    private static final String NULL = "null";
    private static final String BLOB = "blob";

    static {
        FILE_EXTENSIONS.put(MediaType.IMAGE_JPEG_VALUE, "jpg");
        FILE_EXTENSIONS.put(MediaType.IMAGE_PNG_VALUE, "png");
        FILE_EXTENSIONS.put(MediaType.IMAGE_GIF_VALUE, "gif");
    }

    private final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final AlfrescoProperties alfrescoProperties;
    private final AttachmentRepository attachmentRepository;
    private final RetailOrderRepository orderRepository;
    private final BpmAdapterProxy bpmAdapterProxy;

    @Autowired
    public StorageServiceImpl(AlfrescoProperties alfrescoProperties, AttachmentRepository attachmentRepository, RetailOrderRepository orderRepository, BpmAdapterProxy bpmAdapterProxy) {
        this.alfrescoProperties = alfrescoProperties;
        this.attachmentRepository = attachmentRepository;
        this.orderRepository = orderRepository;
        this.bpmAdapterProxy = bpmAdapterProxy;
    }

    private AlfrescoConfig getAlfrescoConfig(){
        AlfrescoProperties.Config config = alfrescoProperties.getConfig();
        logger.debug("AlfrescoProperties.Config {}", config);
        AlfrescoConfig alfrescoConfig = new AlfrescoConfig();
        alfrescoConfig.setProtocol(config.getProtocol());
        alfrescoConfig.setHost(config.getHost());
        alfrescoConfig.setPort(config.getPort());
        alfrescoConfig.setApiUri(config.getApiUri());
        alfrescoConfig.setSrvcUser(config.getUserName());
        alfrescoConfig.setSrvcUserPassword(config.getUserPassword());
        return alfrescoConfig;
    }

    private String getContainerPath(Long orderId){
        Optional<AlfrescoOrder> optional = orderRepository.findByIdEquals(orderId);
        AlfrescoOrder alfrescoOrder = optional.orElseThrow(()-> new EntityNotFoundException(String.format("Order with id=[%s] not found",orderId)));

        return String.format("%s/%s/%s/%s_%s",alfrescoProperties.getCreditPilPath(), alfrescoOrder.getOrderDate().getYear()
                ,alfrescoOrder.getOrderDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"))
                ,alfrescoOrder.getId()
                ,alfrescoOrder.getIin()
            );
    }

    private String saveToStorage(String catalog, String fileName, String contentType, byte[] data){
        logger.debug("saveToStorage catalog=[{}] fileName=[{}] contentType=[{}] bytes=[{}]", catalog, fileName, contentType, data.length);
        String fileNodeRef;
        AlfrescoExplorer client = new AlfrescoExplorer(getAlfrescoConfig());
        boolean hasTicket = false;
        try {
            client.setMainSiteName(alfrescoProperties.getMainSiteName());
            hasTicket = true;
            fileNodeRef = client.uploadFile(catalog, fileName, contentType, data);
        } catch (Exception e) {
            logger.error("Exception during saveToStorage", e);
            throw new AlfrescoFileException(String.format("Ошибка сохранения в ALfresco %s", e.getMessage()), e);
        }
        finally {
            if (hasTicket){
                try {
                    client.logout();
                } catch (Exception e) {
                    logger.error(String.format("AlfrescoClientLogOutException: %s", e.getMessage()), e);
                }
            }
        }
        return fileNodeRef;
    }

    private byte[] getFromStorage(String fileNodeRef){
        logger.debug("getFromStorage fileNoderef=[{}]", fileNodeRef);
        AlfrescoExplorer client = new AlfrescoExplorer(getAlfrescoConfig());
        boolean hasTicket = false;
        byte[] bytes;
        try {
            client.setMainSiteName(alfrescoProperties.getMainSiteName());
            hasTicket = true;
            AlfrescoFile alfrescoFile = client.getFileContent(fileNodeRef);
            bytes = alfrescoFile.getBytes();
        } catch (Exception e) {
            logger.error("Exception during getFromStorage", e);
            throw new AlfrescoFileException(String.format("Ошибка получения данных с Alfresco. %s", e.getMessage()), e);
        }
        finally {
            if (hasTicket){
                try {
                    client.logout();
                } catch (Exception e) {
                    logger.error(String.format("AlfrescoClientLogOutException: %s", e.getMessage()), e);
                }
            }
        }
        return bytes;
    }

    @Override
    @Transactional(readOnly = true, timeout = 10)
    public Optional<Attachment> getOne(Long id) {
        logger.debug("Attachment getOne on ID[{}]", id);
        if (id == null)
            throw new IllegalArgumentException("To get Attachment, Id must not be null");
        return attachmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true, timeout = 10)
    public Optional<Attachment> getOrderAttachment(Long orderId, AttachmentType attachmentType) {
        logger.debug("getOrderAttachment on Order[{}] with AttachmentType[{}]", orderId, attachmentType);
        if (orderId == null)
            throw new IllegalArgumentException("To getOrderAttachment orderId must not be null");
        if (attachmentType == null)
            throw new IllegalArgumentException("To getOrderAttachment attachmentType must not be null");
        return attachmentRepository.findByOrderIdAndAttachmentType(orderId, attachmentType);
    }

    @Override
    public Attachment storeOrderFile(Long orderId, AttachmentType attachmentType, MultipartFile file) throws IOException {
        logger.debug("storeOrderFile on Order[{}] with AttachmentType[{}]", orderId, attachmentType);
        if (file == null)
            throw new IllegalArgumentException("To storeOrderFile file must not be null");
        logger.debug("MultipartFile Name=[{}] FileName=[{}] ContentType=[{}] Empty=[{}] Size=[{}]",
                file.getName(), file.getOriginalFilename(), file.getContentType(), file.isEmpty(), file.getSize());
        String originalFileName =
                BLOB.equalsIgnoreCase(file.getOriginalFilename()) ?
                String.format("%s.%s",file.getOriginalFilename(), FILE_EXTENSIONS.get(file.getContentType()))
                :
                file.getOriginalFilename();
        if (orderId == null)
            throw new IllegalArgumentException("To storeOrderFile orderId must not be null");
        if (attachmentType == null)
            throw new IllegalArgumentException("To storeOrderFile attachmentType must not be null");
        if (file.isEmpty()) {
            throw new BadRequestException(String.format("File is empty to store on Order[%s] with attachmentType[%s]", orderId, attachmentType));
        }
        if (StringUtils.isEmpty(originalFileName)) {
            throw new BadRequestException(String.format("FileName is empty to store on Order[%s] with attachmentType[%s]", orderId, attachmentType));
        }

        String processId = orderRepository.getOrderProcessId(orderId);
        List<Task> receivedTasks = bpmAdapterProxy.getReceivedTasks(processId);
        if (receivedTasks.isEmpty())
            throw new InternalServerException(String.format("Нет задач в BPM со статусом  received для заявки с Id=%s и processid=%s", orderId, processId));

        Task task = receivedTasks.stream()
                .filter(t-> RetailOrderUiStep.UPDATE_INFO.getStepName().equals(t.getName())
                        || attachmentType.getStep().getStepName().equals(t.getName()))
                .findFirst()
                .orElseThrow(()-> new InternalServerException(String.format("Задача для загрузки документа %s или %s не найдена в BPM orderId=%s and processId=%s", attachmentType, RetailOrderUiStep.UPDATE_INFO.getStepName(), orderId, processId)))
                ;

        byte[] data = file.getBytes();
        String catalog = getContainerPath(orderId);

        String mimeType = StringUtils.isEmpty(file.getContentType()) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : file.getContentType();
        String fileExtension = StringUtils.getFilenameExtension(originalFileName).toLowerCase();
        if (StringUtils.isEmpty(fileExtension) || NULL.equalsIgnoreCase(fileExtension)) {
            throw new BadRequestException(String.format("расширение файла пустое для сохранения заявки [%s] with attachmentType[%s], filename=[%s] contentType=[%s] fileExtension=[%s]", orderId, attachmentType, file.getOriginalFilename(), file.getContentType(), fileExtension));
        }

        String fileNodeRef = null;
        Attachment attachment = getOrderAttachment(orderId, attachmentType)
                    .orElse(new Attachment(orderId, attachmentType));
        attachment.setContentType(mimeType);
        attachment.setCatalog(catalog);
        attachment.setFileName(String.format("%s_%s.%s", attachmentType, orderId.toString(), fileExtension));
        attachment.setOriginalFilename(originalFileName);
        attachment.setFileNameExtension(fileExtension);
        attachment.setFileNodeRef(fileNodeRef);
        logger.debug("Attachment to storeOrderFile {}", attachment);
        attachment = attachmentRepository.save(attachment);

        fileNodeRef = saveToStorage(attachment.getCatalog(), attachment.getFileName(), attachment.getContentType(), data);
        attachment.setFileNodeRef(fileNodeRef);

        attachment = attachmentRepository.saveAndFlush(attachment);
        if (
                !( task.getName().equals(RetailOrderUiStep.UPDATE_INFO.getStepName()) || task.getName().equals(RetailOrderUiStep.PASSPORT_PHOTO.getStepName()))
            )
        {
            orderRepository.setProcessingStepUI(orderId, SecurityContextHolder.getContext().getAuthentication().getName());
            bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
        }

        return attachment;
    }

    @Override
    public StorageContent getFileContent(Long id){
        logger.debug("getFileContent on Attachment[{}]", id);
        if (id == null)
            throw new IllegalArgumentException("To getFileContent AttachmentId must not be null");
        Attachment attachment = getOne(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Attachment with id=%s not found",id)));
        String fileNodeRef = attachment.getFileNodeRef();
        if (StringUtils.isEmpty(fileNodeRef))
            throw new InternalServerException(String.format("For AttachmentId[%s] fileNodeRef is null or empty", id));

        byte[] data = getFromStorage(fileNodeRef);
        return new StorageContent(data, attachment.getContentType(), attachment.getFileName());
    }

    @Override
    public StorageContent getOrderFileContent(Long orderId, AttachmentType attachmentType){
        logger.debug("getOrderFileContent on Order[{}] AttachmentType[{}]", orderId, attachmentType);
        if (orderId == null)
            throw new IllegalArgumentException("To getOrderFileContent orderId must not be null");
        if (attachmentType == null)
            throw new IllegalArgumentException("To storeOrderFile attachmentType must not be null");
        Attachment attachment = getOrderAttachment(orderId, attachmentType)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Attachment with orderId=%s and attachmentType=%s not found",orderId,attachmentType)));
        String fileNodeRef = attachment.getFileNodeRef();
        if (StringUtils.isEmpty(fileNodeRef))
            throw new InternalServerException(String.format("For Attachment with orderId=%s and attachmentType=%s fileNodeRef is null or empty", orderId,attachmentType));

        byte[] data = getFromStorage(fileNodeRef);
        return new StorageContent(data, attachment.getContentType(), attachment.getFileName());
    }

    @Override
    public void completePassportPhoto(Long orderId) {
        logger.debug("completePassportPhoto on OrderId[{}]", orderId);
        String processId = orderRepository.getOrderProcessId(orderId);
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, RetailOrderUiStep.PASSPORT_PHOTO.getStepName());
        List<AttachmentType> list =  Arrays.asList(AttachmentType.IDCARD_FRONTSIDE, AttachmentType.IDCARD_BACKSIDE);
        Integer count = attachmentRepository.countAllByOrderIdAndAttachmentTypeIn(orderId, list);
        if (count == null || count < list.size())
            throw new InternalServerException(String.format("Не все документы подгружены, нужна лицевая и обратная сторона(PassportPhoto) for orderId=%s", orderId));
        orderRepository.setProcessingStepUI(orderId, SecurityContextHolder.getContext().getAuthentication().getName());
        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
    }
}
