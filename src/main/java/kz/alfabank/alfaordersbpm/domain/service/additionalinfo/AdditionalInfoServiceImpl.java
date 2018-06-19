package kz.alfabank.alfaordersbpm.domain.service.additionalinfo;

import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.additionalinfo.AdditionalInfo;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.contactperson.ContactPerson;
import kz.alfabank.alfaordersbpm.domain.models.dto.AdditionalInfoDTO;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.phone.PhoneType;
import kz.alfabank.alfaordersbpm.domain.repositories.AdditionalInfoRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.commonservice.CommonServiceProxy;
import kz.alfabank.alfaordersbpm.domain.service.contactperson.ContactPersonService;
import kz.alfabank.alfaordersbpm.domain.service.phone.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(timeout = 45, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AdditionalInfoServiceImpl implements AdditionalInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalInfoServiceImpl.class);

    @Autowired
    private AdditionalInfoRepository infoRepository;
    @Autowired
    private ContactPersonService contactPersonService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CommonServiceProxy commonServiceProxy;
    @Autowired
    private BpmCrmProperties bpmCrmProperties;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private BpmAdapterProxy bpmAdapterProxy;
    @Autowired
    private RetailOrderRepository orderRepository;



    @Override
    @Transactional(timeout = 15, readOnly = true)
    public Optional<AdditionalInfo> getByOrderId(Long orderId) {
        LOG.debug("AdditionalInfoService getByOrderId orderId={}", orderId);
        return infoRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional(timeout = 15, readOnly = true)
    public Optional<AdditionalInfo> getOne(Long id) {
        LOG.debug("AdditionalInfoService getOne id={}", id);
        return infoRepository.findById(id);
    }

    @Override
    public AdditionalInfo save(AdditionalInfo entity) {
        LOG.debug("AdditionalInfoService save Entity={}", entity);
        return infoRepository.saveAndFlush(entity);
    }

    @Override
    public AdditionalInfo fromAddInfoDTO(Long orderId, AdditionalInfoDTO infoDTO){
        LOG.debug("AdditionalInfoService fromAddInfoDTO orderId={} {}", orderId, infoDTO);
        if (orderId == null)
            throw new IllegalArgumentException("Для создания адреса, идентификатор заявки не может быть null");
        String processId = orderRepository.getOrderProcessId(orderId);
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, RetailOrderUiStep.PERSONAL_INFO.getStepName());
        AdditionalInfo info = orderMapper.fromAddInfoDTO(orderId, infoDTO);
        List<ContactPerson> contactPeople = orderMapper.getContactPersonsFromAddInfoDTO(orderId, infoDTO);
        String phoneValue = infoDTO.getHomePhone();
        if (!StringUtils.isEmpty(phoneValue)){
            CommonServiceRef homephoneRef = commonServiceProxy.getDictValByCodeSync(bpmCrmProperties.getDictionaries().getDefaultValues().getPhoneTypeDictName(), bpmCrmProperties.getDictionaries().getDefaultValues().getHomePhoneCode());
            Phone homePhone = new Phone();
            homePhone.setOrderId(orderId);
            homePhone.setPhone(phoneValue);
            homePhone.setPhoneType(PhoneType.HOME);
            homePhone.setPhoneTypeRef(homephoneRef);
            phoneService.save(homePhone);
        }
        contactPersonService.saveAll(contactPeople);
        AdditionalInfo additionalInfo = infoRepository.save(info);
        orderRepository.setProcessingStepUI(orderId, SecurityContextHolder.getContext().getAuthentication().getName());
        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
        return additionalInfo;
    }

}
