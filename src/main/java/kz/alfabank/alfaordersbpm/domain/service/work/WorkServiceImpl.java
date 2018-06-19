package kz.alfabank.alfaordersbpm.domain.service.work;
import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.address.Address;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.dto.WorkDetailsDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.CommonServiceException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.work.Work;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.WorkRepository;
import kz.alfabank.alfaordersbpm.domain.service.address.AddressService;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.commonservice.CommonServiceProxy;
import kz.alfabank.alfaordersbpm.domain.service.phone.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(timeout = 45, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class WorkServiceImpl implements WorkService{

    private static final Logger LOG = LoggerFactory.getLogger(WorkServiceImpl.class);

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private CommonServiceProxy commonServiceProxy;

    @Autowired
    private BpmCrmProperties bpmCrmProperties;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private BpmAdapterProxy bpmAdapterProxy;

    @Autowired
    private RetailOrderRepository orderRepository;


    @Override
    @Transactional(readOnly = true,timeout = 10)
    public Optional<Work> getOne(Long id) {
        LOG.debug("WorkService getOne ID={}", id);
        return workRepository.findById(id);
    }

    @Override
    public Work save(Work entity) {
        LOG.debug("WorkService save {}", entity);
        return workRepository.saveAndFlush(entity);
    }

    @Override
    public Work update(Work entity) {
        LOG.debug("WorkService update {}", entity);
        Long id = entity.getId();
        if(id == null)
            throw new BadRequestException(String.format("To update, Work must have an ID, Work=%s", entity.toString()));
        if(!existsById(id))
            throw new EntityNotFoundException(String.format("Work with ID=%s not found to perform update, Work=%s", id, entity.toString()));
        return save(entity);
    }

    @Override
    public Work insert(Work entity) {
        LOG.debug("WorkService insert {}", entity);
        Long id = entity.getId();
        if(id != null)
            throw new BadRequestException(String.format("To perform insert, Work ID must be null, passed Work=%s", entity.toString()));
        return save(entity);
    }

    @Override
    public Work fromWorkDetails(Long orderId, WorkDetailsDTO workDetailsDTO) {
        LOG.debug("WorkService fromWorkDetails orderId={} {}", orderId, workDetailsDTO);
        if (orderId == null)
            throw new IllegalArgumentException("WorkService->fromWorkDetails orderId is null");
        if (workDetailsDTO == null)
            throw new IllegalArgumentException("WorkService->fromWorkDetails workDetailsDTO is null");

        String processId = orderRepository.getOrderProcessId(orderId);
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, RetailOrderUiStep.JOB.getStepName());

        Address address = orderMapper.fromWorkDetailsToAddress(workDetailsDTO);
        String addrDictName = bpmCrmProperties.getDictionaries().getDefaultValues().getAddressTypeDictName();
        String addrWorkCode = bpmCrmProperties.getDictionaries().getDefaultValues().getWorkAddressCode();
        CompletableFuture<Optional<CommonServiceRef>> refFutureAddr = commonServiceProxy.getDictValByCode(addrDictName, addrWorkCode);

        Phone phone = orderMapper.fromWorkDetailsToPhone(workDetailsDTO);
        String phoneDictName = bpmCrmProperties.getDictionaries().getDefaultValues().getPhoneTypeDictName();
        String workPhoneTypeCode = bpmCrmProperties.getDictionaries().getDefaultValues().getWorkPhoneCode();
        CompletableFuture<Optional<CommonServiceRef>> refFuturePhone = commonServiceProxy.getDictValByCode(phoneDictName, workPhoneTypeCode);

        address.setOrderId(orderId);
        phone.setOrderId(orderId);

        Optional<CommonServiceRef> addrref;
        try {
            addrref = refFutureAddr.get();
        } catch (Exception e) {
            throw new InternalServerException("WorkService->fromWorkDetails calling get CommonService Result " + e.getMessage(), e);
        }

        if (!addrref.isPresent())
            throw new CommonServiceException(String.format("WorkService->fromWorkDetails value with code [%s] not found in dictionary %s",addrWorkCode,addrDictName));

        LOG.debug("WorkService fromWorkDetails addrref={}", addrref);
        address.setAddressTypeRef(addrref.get());

        Optional<CommonServiceRef> phoneref;
        try {
            phoneref = refFuturePhone.get();
        } catch (Exception e) {
            throw new InternalServerException("WorkService->fromWorkDetails calling get CommonService Result " + e.getMessage(), e);
        }

        if (!phoneref.isPresent())
            throw new CommonServiceException(String.format("WorkService->fromWorkDetails value with code [%s] not found in dictionary %s",workPhoneTypeCode,phoneDictName));


        phone.setPhoneTypeRef(phoneref.get());

        Work work = orderMapper.fromWorkDetails(workDetailsDTO);
        work.setOrderId(orderId);

        addressService.save(address);
        phoneService.save(phone);

        work = save(work);
        orderRepository.setProcessingStepUI(orderId, SecurityContextHolder.getContext().getAuthentication().getName());
        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
        return work;
    }

    @Override
    @Transactional(readOnly = true,timeout = 15)
    public boolean existsById(Long id) {
        LOG.debug("WorkService existsById ID={}", id);
        return workRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("WorkService delete by ID={}", id);
        workRepository.deleteById(id);
    }

    @Override
    public Optional<Work> getByOrderId(Long orderId) {
        LOG.debug("WorkService getByOrderId orderId={}", orderId);
        if (orderId == null)
            throw new IllegalArgumentException("WorkService getByOrderId OrderId is null");
        return workRepository.findByOrderId(orderId);
    }
}
