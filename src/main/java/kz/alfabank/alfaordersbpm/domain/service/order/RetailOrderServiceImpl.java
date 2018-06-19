package kz.alfabank.alfaordersbpm.domain.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.alfabank.alfaordersbpm.components.XmlMapper;
import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.*;
import kz.alfabank.alfaordersbpm.domain.models.bpm.event.EventMsg;
import kz.alfabank.alfaordersbpm.domain.models.dto.ApprovedConditionsDTO;
import kz.alfabank.alfaordersbpm.domain.models.dto.PersonDetailsDTO;
import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmationCheck;
import kz.alfabank.alfaordersbpm.domain.models.dto.PilOrderConditionDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.CommonServiceException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.models.mappers.CommonServiceMapper;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValAttrResponse;
import kz.alfabank.alfaordersbpm.domain.models.order.*;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.MyRetailOrders;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.OrderAuditDetails;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.OrderAuditOperations;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.ProcAppInfo;
import kz.alfabank.alfaordersbpm.domain.models.scoring.ApprovedConditions;
import kz.alfabank.alfaordersbpm.domain.repositories.ApprovedConditionsRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.OrderInProgressRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.address.AddressService;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.commonservice.CommonServiceProxy;
import kz.alfabank.alfaordersbpm.domain.service.phone.PhoneService;
import kz.alfabank.alfaordersbpm.domain.service.smsconfirmation.SmsConfirmationService;
import kz.alfabank.alfaordersbpm.util.RequestUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(timeout =60, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class RetailOrderServiceImpl implements RetailOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(RetailOrderServiceImpl.class);
    private static final int DEFAULT_PAGE_SIZE = 30;
    private static final int FIRST_PAGE_NO = 0;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "id");
    private static final String FIRST_STEP_NAME = "NEW";
    private static final String DECLINED = "decline";
    private static final String PROCESSING_STEP_PREFIX = "Обработка: ";
    private static final String ENTITY_NAME = "PR_PIL_ORDERS";
    private static final String CANCEL_STEP_UI = "Обработка: отказ клиента";

    @Autowired
    private RetailOrderRepository retailOrderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CommonServiceProxy commonServiceProxy;
    @Autowired
    private CommonServiceMapper commonServiceMapper;
    @Autowired
    private SmsConfirmationService confirmationService;
    @Autowired
    private BpmCrmProperties bpmCrmProperties;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private OrderInProgressRepository progressRepository;
    @Autowired
    private AuditorAware<String> auditorAware;
    @Autowired
    private BpmAdapterProxy bpmAdapterProxy;
    @Autowired
    private ApprovedConditionsRepository conditionsRepository;
    @Autowired
    private XmlMapper xmlMapper;




    @Override
    public Page<MyRetailOrders> pageMyOrders(LocalDate startDate, LocalDate endDate, OrderStateFilter stateFilter, Pageable pageable) {
        int pageNo = pageable==null? FIRST_PAGE_NO : Math.max(pageable.getPageNumber(),FIRST_PAGE_NO); // Handle less than zero values
        int pageSize = pageable==null? DEFAULT_PAGE_SIZE : Math.max(pageable.getPageSize(),1); // Handle less than 1 values for size
        Sort sort = pageable==null? DEFAULT_SORT : pageable.getSortOr(DEFAULT_SORT);
        LOG.debug("pageMyOrders. pageNo={}, pageSize={}, sort={}",pageNo,pageSize,sort);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<String> grantees = new ArrayList<>(authorities.size() + 1);
        grantees.add(userName.toUpperCase());
        for (GrantedAuthority var : authorities){
            grantees.add(var.getAuthority().toUpperCase());
        }

        if (OrderStateFilter.ACTIVE.equals(stateFilter) || OrderStateFilter.COMPLETED.equals(stateFilter)){
            boolean isActive = OrderStateFilter.ACTIVE.equals(stateFilter);
            return retailOrderRepository.findAllMyActiveFlag(grantees, RequestUtil.getOrgCode(), startDate, endDate, isActive, PageRequest.of(pageNo, pageSize, sort));
        }
        else if (OrderStateFilter.ALL.equals(stateFilter)){
            return retailOrderRepository.findAllMy(grantees, RequestUtil.getOrgCode(), startDate, endDate, PageRequest.of(pageNo, pageSize, sort));
        }
        else if (OrderStateFilter.FAULTED.equals(stateFilter)) {
            return retailOrderRepository.pageAllByOrderState(grantees, RequestUtil.getOrgCode(), startDate, endDate, OrderState.FAULTED, PageRequest.of(pageNo, pageSize, sort));
        }

        throw new BadRequestException(String.format("Нет обработчика для фильтра=%s", stateFilter.name()));
    }

    @Override
    @Transactional(readOnly = true,timeout = 20)
    public Optional<RetailOrder> getOne(Long id) {
        LOG.debug("RetailOrder getOne ID={}",id);
        return retailOrderRepository.findById(id);
    }

    @Override
    public Optional<ProcAppInfo> getByRequestId(Long requestId) {
        LOG.debug("RetailOrder getByRequestId ID={}", requestId);
        return retailOrderRepository.findByRequestId(requestId);
    }

    @Override
    @Transactional(readOnly = true,timeout = 25)
    public Optional<RetailOrder> getOne(RetailOrder entity) {
        LOG.debug("RetailOrder getOne Entity={}",entity);
        return getOne(entity.getId());
    }

    private void generateOrderNum(RetailOrder order){
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            String s = retailOrderRepository.getOrderNum(order.getClass().getSimpleName())
                    .orElseThrow(()-> new InternalServerException("Ошибка при генерации номера заявки generateOrderNum, function result is null or empty"))
                    ;
            order.setOrderNumber(s);
        }
    }

    private static void setUpOrderState(AbstractOrder entity){
        if (entity.getOrderState() == null)
            entity.setOrderState(OrderState.CREATED);

        switch (entity.getOrderState()){
            case CREATED:
                entity.setActive(true);
                entity.setCancelable(true);
                break;
            case RUNNING:
                entity.setActive(true);
                entity.setCancelable(true);
                entity.setStarted(true);
                break;
            case REFUSED:
            case CANCELED:
            case COMPLETED:
                entity.setActive(false);
                entity.setCancelable(false);
                break;
            case FAULTED:
                entity.setCancelable(true);
                break;

            default:
                break;
        }
    }

    private void processOrderState(RetailOrder entity){
        if (OrderState.getUnmodifiableTerminatedStates().contains(entity.getOrderState())){
            progressRepository.deleteByOrderId(entity.getId());
        }
    }

    @Override
    public RetailOrder save(RetailOrder entity) {
        LOG.debug("RetailOrder save Entity={}",entity);
        if (entity.getOrderDate() == null)
            entity.setOrderDate(LocalDate.now());
        generateOrderNum(entity);
        setUpOrderState(entity);
        RetailOrder order = retailOrderRepository.saveAndFlush(entity);
        processOrderState(order);
        return order;
    }

    @Override
    public RetailOrder update(RetailOrder entity) {
        LOG.debug("RetailOrder update Entity={}",entity);
        Long id = entity.getId();
        if(id == null)
            throw new InvalidDataAccessApiUsageException(String.format("To update, entity must have an ID, EntityToString=%s", entity.toString()));
        if(!existsById(id))
            throw new EmptyResultDataAccessException(String.format("Заявка с ID=%s не найдена, чтобы обновить данные, EntityToString=%s", id, entity.getClass().getSimpleName()),1);
        return save(entity);
    }

    @Override
    public RetailOrder insert(RetailOrder entity) {
        LOG.debug("RetailOrder insert Entity={}",entity);
        Long id = entity.getId();
        if(id != null)
            throw new InvalidDataAccessApiUsageException(String.format("To perform insert, entity ID must be null, passed EntityToString=%s", entity.getClass().getSimpleName()));
        RetailOrder order = save(entity);

        BpmInputRequest bpmRequest = new BpmInputRequest(order.getId().toString(), null, null
                , order.getCorrelationId()
                , UUID.randomUUID().toString());
        ProcessInfoResponse processResponse  =bpmAdapterProxy.startProcess(bpmRequest);
        ProcessData processData = processResponse.getProcessData();
        Map<String, Object> varibales = processData.getVariables();
        String step = varibales.get(ProcessData.STEP_UI).toString();

        Long requestId = bpmAdapterProxy.createRequest(processData.getPiid(), order.getCreatedBy(), ENTITY_NAME);
        order.setPiid(processData.getPiid());
        order.setStepUI(step);
        order.setRequestId(requestId);
        order = save(order);

        return order;
    }

    @Override
    public RetailOrder fromCondition(PilOrderConditionDTO conditionDTO) {
        LOG.debug("RetailOrder fromCondition Entity={}",conditionDTO);
        String userName = auditorAware.getCurrentAuditor().orElseThrow(()-> new InternalServerException("Ошибка получения текущего пользователя"));
        LOG.debug("Got UserName={}",userName);
        String orgCode = RequestUtil.getOrgCode();
        String orgName = RequestUtil.getOrgName();
        LOG.debug("Got orgCode={} orgName={}", orgCode, orgName);
        if (StringUtils.isEmpty(orgCode))
            throw new BadRequestException("Код подразделения равен null или пуст");
        if (StringUtils.isEmpty(orgName))
            throw new BadRequestException("Наименование подразделения равно null или пусто");

        CompletableFuture<DictValAttrResponse> productAttrsResp =
                commonServiceProxy.getDictValAttrs(bpmCrmProperties.getDictionaries().getDefaultValues().getProductDictName(), conditionDTO.getCreditProductRef().getValue());
        RetailOrder order = orderMapper.condtionDTOToPilOrder(conditionDTO);
        DictValAttrResponse productAtr;
        try {
            productAtr = productAttrsResp.get();
        } catch (Exception ex) {
            LOG.error("fromCondition->get Attrs", ex);
            throw new CommonServiceException("Ошибка получения аттрибутов общего справочника BPMOCRM для продукта"+ex);
        }
        Map<String, String> attrs = commonServiceMapper.convertDictAttrsToMap(productAtr);
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(attrs.get("RATE").replace(",",".")));
        String experianCode = attrs.get("EXPERIAN_CODE");
        String colvirCode = attrs.get("COLVIR_CODE");
        order.setRate(rate);
        order.setExperianCode(experianCode);
        order.setColvirCode(colvirCode);
        order.setProductAttributes(attrs);
        order.setCorrelationId(UUID.randomUUID().toString());
        order.setStepUI(FIRST_STEP_NAME);
        order.setAssignedTo(userName);
        order.setOrgCode(orgCode);
        order.setOrgName(orgName);

        String purposeDictName = bpmCrmProperties.getDictionaries().getDefaultValues().getCreditPurposeDictName();
        String withoutInsuranceCode = bpmCrmProperties.getDictionaries().getDefaultValues().getWithoutInsuranceCode();
        String creditPurposeCode;
        CompletableFuture<Optional<CommonServiceRef>> refFuturePurpose;
        String insuranceCode = conditionDTO.getInsuranceRef().getValue();
        LOG.debug("Got insuranceCode={}",insuranceCode);
        if(insuranceCode.equals(withoutInsuranceCode)){
            creditPurposeCode = bpmCrmProperties.getDictionaries().getDefaultValues().getCreditPurposeCode();
            LOG.debug("Got creditPurposeCode={}",creditPurposeCode);
            refFuturePurpose = commonServiceProxy.getDictValByCode(purposeDictName, creditPurposeCode);
        } else {
            creditPurposeCode = bpmCrmProperties.getDictionaries().getDefaultValues().getInsuranceCreditPurposeCode();
            LOG.debug("Got creditPurposeCode={}",creditPurposeCode);
            refFuturePurpose = commonServiceProxy.getDictValByCode(purposeDictName, creditPurposeCode);
        }

        Optional<CommonServiceRef> purposeRef;
        try {
            purposeRef = refFuturePurpose.get();
        } catch (Exception e) {
            throw new InternalServerException("Ошибка получения общего справочника BPMOCRM по цели кредита" + e.getMessage(), e);
        }

        if (!purposeRef.isPresent())
            throw new CommonServiceException(String.format("Ошибка получения общего справочника BPMOCRM код [%s] не найден в справочнике %s", creditPurposeCode, purposeDictName));

        order.setCreditPurposeRef(purposeRef.get());

        return insert(order);
    }

    @Override
    public RetailOrder updatePersonDetails(Long id, PersonDetailsDTO personDetailsDTO) {
        LOG.debug("RetailOrder updatePersonDetails PersonDetails={}", personDetailsDTO);
        progressRepository.findByIin(personDetailsDTO.getIin())
                .ifPresent(o-> {
                    throw new InternalServerException(String.format("Найдена активная заявка с ИИН=[%s], OrderId=[%s] Id=[%s]",personDetailsDTO.getIin(), o.getOrderId(), o.getId()));
                });
        Optional<RetailOrder> t = getOne(id);
        if (!t.isPresent())
            throw new EntityNotFoundException(String.format("Заявка с ID=%s не найдена для обновления данными клиента", id));
        RetailOrder order = t.get();

        RetailOrderUiStep taskUI = RetailOrderUiStep.CUSTOMER;
        Task task = getOrderTask(order, taskUI);

        PhoneConfirmationCheck checkResult = confirmationService.checkSmsConfirmation(order.getId(),personDetailsDTO.getMobilePhone(),personDetailsDTO.getConfirmationCode());
        if(!checkResult.isValid())
            throw new BadRequestException(String.format("Проверка кода подтверждения не пройдена, %s", checkResult.getMessage()));
        orderMapper.mapPersonDetailsToPilOrder(order, personDetailsDTO);
        order.setOrderState(OrderState.RUNNING);

        OrderInProgress inProgress = OrderInProgress.of(order.getId(), order.getIin());
        progressRepository.saveAndFlush(inProgress);

        order.setStepUI(PROCESSING_STEP_PREFIX + order.getStepUI());
        order = save(order);
        addressService.fromPersonDetails(order.getId(),personDetailsDTO);
        phoneService.fromPersonDetails(order.getId(),personDetailsDTO);

        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task.getTkiid(), taskUI.getStepName()));
        return order;
    }

    @Override
    @Transactional(readOnly = true,timeout = 20)
    public boolean existsById(Long id) {
        LOG.debug("RetailOrder existsById ID={}", id);
        return retailOrderRepository.existsById(id);
    }

    @Override
    public void delete(RetailOrder entity) {
        LOG.debug("RetailOrder delete Entity={}", entity);
        if (entity == null || entity.getId() == null)
            throw new EntityNotFoundException(String.format("Order is null or ID is null Entity=%s", entity));
        if (BooleanUtils.isFalse(entity.isCancelable()))
            throw new BadRequestException(String.format("Заявку невозможно отменить в данный момент, state=%s ", entity.getOrderState()));
        entity.setOrderState(OrderState.CANCELED);
        entity.setStepUI(CANCEL_STEP_UI);
        save(entity);
        EventMsg eventMsg = EventMsg.of(entity);
        LOG.debug("EventMsg ={}", eventMsg);
        String messsage;
        try {
            messsage = xmlMapper.createMessage(eventMsg);
        } catch (JsonProcessingException e) {
            LOG.error("Exception in cancel xmlMapper.createMessage", e);
            throw new InternalServerException("Exception in xmlMapper createMessage" + e.getMessage());
        }
        bpmAdapterProxy.sendMessage(messsage);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("RetailOrder delete ID={}", id);
        Optional<RetailOrder> optional = retailOrderRepository.findById(id);
        if (!optional.isPresent()){
            throw new EntityNotFoundException(String.format("Заявка с id=%s не найдена для отмены", id));
        }
        delete(optional.get());
    }

    @Override
    public Optional<OrderInProgress> findOrderInProgress(String iin){
        return progressRepository.findByIin(iin);
    }

    @Override
    public List<OrderAuditOperations> getAuditOperations(Long id) {
        LOG.debug("RetailOrder getAuditOperations ID={}", id);
        return retailOrderRepository.getOrderAuditOperations(id);
    }

    @Override
    public List<OrderAuditDetails> getOrderAuditDetails(Long auditId) {
        LOG.debug("RetailOrder getOrderAuditDetails auditId={}", auditId);
        return retailOrderRepository.getOrderAuditDetails(auditId);
    }

    @Override
    public String getProcessStepUI(RetailOrder entity) {
        LOG.debug("RetailOrder getProcessStepUI entity={}", entity);
        return bpmAdapterProxy.getProcessStepUI(entity.getPiid());
    }

    @Override
    public Task getOrderTask(RetailOrder entity, RetailOrderUiStep uiStep) {
        LOG.debug("RetailOrder getOrderTask entityId={} processId={} taskName={}", entity.getId(), entity.getPiid(), uiStep.getStepName());
        return bpmAdapterProxy.getReceivedTaskByName(entity.getPiid(), uiStep.getStepName())
                .orElseThrow(()-> new InternalServerException(String.format("Задача в статусе Received для processId=%s с наименованием=%s не найдена в BPM", entity.getPiid(), uiStep.getStepName())));
    }

    @Override
    @Transactional(readOnly = true, timeout = 20)
    public Optional<ApprovedConditions> getApprovedConditions(Long id) {
        LOG.debug("RetailOrder getApprovedConditions ID={}", id);
        return conditionsRepository.findByOrderId(id);
    }

    @Override
    public ApprovedConditions fromApprovedConditionsDTO(Long id, @Valid ApprovedConditionsDTO conditionsDTO) {
        LOG.debug("RetailOrder fromApprovedConditionsDTO ID={}", id);
        String processId = retailOrderRepository.getOrderProcessId(id);
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, RetailOrderUiStep.ALTERNATIVE.getStepName());
        ApprovedConditions conditions = getApprovedConditions(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Одобренные условия для заявки с id=%s не найдены", id)));
        if (conditions.getDecisionCategory().equalsIgnoreCase(DECLINED))
            throw new BadRequestException(String.format("Заявка Отказана %s, нельзя совершить никаких операций кроме отмены", DECLINED));
        boolean hasChanged = !conditions.getApprovedDuration().equals(conditionsDTO.getApprovedDuration());
        conditions.setOverPayment(conditionsDTO.getOverPayment());
        conditions.setGesv(conditionsDTO.getGesv());
        conditions.setApprovedDuration(conditionsDTO.getApprovedDuration());
        conditions = conditionsRepository.saveAndFlush(conditions);
        retailOrderRepository.setProcessingStepUI(id, SecurityContextHolder.getContext().getAuthentication().getName());
        if (hasChanged){
            bpmAdapterProxy.completeTask(task, TaskResult.ofSuccess(task.getTkiid(), task.getName(), "CHANGE"));
        } else {
            bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
        }
        return conditions;
    }


}
