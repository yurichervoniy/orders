package kz.alfabank.alfaordersbpm.domain.service.address;

import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.address.Address;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressType;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.dto.AddressDTO;
import kz.alfabank.alfaordersbpm.domain.models.dto.PersonDetailsDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.*;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.repositories.AddressRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.commonservice.CommonServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AddressServiceImpl implements AddressService {

    private static final Logger LOG = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final CommonServiceProxy commonServiceProxy;
    private final BpmCrmProperties bpmCrmProperties;
    private final OrderMapper orderMapper;
    private final BpmAdapterProxy bpmAdapterProxy;
    private final RetailOrderRepository orderRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, CommonServiceProxy commonServiceProxy, BpmCrmProperties bpmCrmProperties, OrderMapper orderMapper, BpmAdapterProxy bpmAdapterProxy, RetailOrderRepository orderRepository) {
        this.addressRepository = addressRepository;
        this.commonServiceProxy = commonServiceProxy;
        this.bpmCrmProperties = bpmCrmProperties;
        this.orderMapper = orderMapper;
        this.bpmAdapterProxy = bpmAdapterProxy;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true,timeout = 20)
    public List<Address> getByOrderId(Long orderId) {
        LOG.debug("AddressService getByOrderId orderId={}", orderId);
        if (orderId == null)
            throw new IllegalArgumentException("Для получения адресов заявки orderId не может быть null");
        return addressRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<Address> getByOrdeIdAndAddresType(Long orderId, AddressType addressType) {
        LOG.debug("AddressService getByOrdeIdAndAddresType orderID={} AddressType={}", orderId, addressType);
        if (orderId == null)
            throw new IllegalArgumentException("Для получения адреса заявки orderId не может быть null");
        if (addressType == null)
            throw new IllegalArgumentException("Для получения адреса заявки AddressType не может быть null");
        return addressRepository.findByOrderIdAndAddressType(orderId,addressType);
    }

    @Override
    @Transactional(readOnly = true,timeout = 10)
    public Optional<Address> getOne(Long id) {
        LOG.debug("AddressService getOne ID={}", id);
        return addressRepository.findById(id);
    }

    @Override
    public Address save(Address entity) {
        LOG.debug("save Address={}", entity);
        return addressRepository.saveAndFlush(entity);
    }

    @Override
    public Address update(Address entity) {
        LOG.debug("update Address={}", entity);
        Long id = entity.getId();
        if(id == null)
            throw new BadRequestException(String.format("Для обновления Адреса ID записи обязателен, Address=%s", entity.toString()));
        if(!existsById(id))
            throw new EntityNotFoundException(String.format("Адрес с ID=%s не найден для осуществления обновления данных, Address=%s", id, entity.toString()));
        return save(entity);
    }

    @Override
    public Address insert(Address entity) {
        LOG.debug("insert Address={}", entity);
        Long id = entity.getId();
        if(id != null)
            throw new BadRequestException(String.format("Для добавления адреса у записи не может быть ID. Address=%s", entity.toString()));
        String processId = orderRepository.getOrderProcessId(entity.getOrderId());
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, RetailOrderUiStep.ADDRESS.getStepName());
        Address result = save(entity);
        orderRepository.setProcessingStepUI(entity.getOrderId(), SecurityContextHolder.getContext().getAuthentication().getName());
        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
        return result;
    }

    @Override
    public Address fromPersonDetails(Long orderId, PersonDetailsDTO personDetailsDTO) {
        LOG.debug("Address fromPersonDetails OrderId={} {}", orderId, personDetailsDTO);
        if (orderId == null)
            throw new IllegalArgumentException("Для добавления информации о клиенте, orderId не может быть null");
        if (personDetailsDTO == null)
            throw new IllegalArgumentException("ля добавления информации о клиенте, personDetailsDTO не может быть null");

        String addrDictName = bpmCrmProperties.getDictionaries().getDefaultValues().getAddressTypeDictName();
        String addrRegistrationCode = bpmCrmProperties.getDictionaries().getDefaultValues().getRegistrationAddressCode();

        CompletableFuture<Optional<CommonServiceRef>> refFuture = commonServiceProxy.getDictValByCode(addrDictName, addrRegistrationCode);

        Address address = orderMapper.personDetailsToAddress(personDetailsDTO);
        address.setOrderId(orderId);
        address.setAddressType(AddressType.REGISTRATION);

        Optional<CommonServiceRef> ref;
        try {
            ref = refFuture.get();
        } catch (Exception e) {
            throw new CommonServiceException("Произошла ошибка получения данных общего справочника BPMOCRM " + e.getMessage(), e);
        }

        if (!ref.isPresent())
            throw new CommonServiceException(String.format("Значение с кодом [%s] не найдено в общем справочнике BPMOCRM %s",addrRegistrationCode,addrDictName));

        address.setAddressTypeRef(ref.get());

        return save(address);
    }

    @Override
    @Transactional(readOnly = true,timeout = 15)
    public boolean existsById(Long id) {
        LOG.debug("Address existsById ID={}", id);
        return addressRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Address delete by ID={}", id);
        addressRepository.deleteById(id);
    }

    @Override
    public Address insertFromAddressDTO(AddressDTO addressDTO) {
        LOG.debug("Address insertFromAddressDTO {}", addressDTO);
        if (addressDTO == null)
            throw new IllegalArgumentException("При вставке адреса AddressDTO не может быть null");

        String addrDictName = bpmCrmProperties.getDictionaries().getDefaultValues().getAddressTypeDictName();
        String addrCrmCode = orderMapper.getCrmCodeForAddressType(addressDTO.getAddressType());
        CompletableFuture<Optional<CommonServiceRef>> refFuture = commonServiceProxy.getDictValByCode(addrDictName, addrCrmCode);

        Address address = orderMapper.fromAddressDTO(addressDTO);

        Optional<CommonServiceRef> ref;
        try {
            ref = refFuture.get();
        } catch (Exception e) {
            throw new InternalServerException("Произошла ошибка получения данных общего справочника BPMOCRM " + e.getMessage(), e);
        }
        if (!ref.isPresent())
            throw new CommonServiceException(String.format("Значение с кодом [%s] не найдено в общем справочнике BPMOCRM %s", addrCrmCode, addrDictName));

        address.setAddressTypeRef(ref.get());
        return insert(address);
    }


}