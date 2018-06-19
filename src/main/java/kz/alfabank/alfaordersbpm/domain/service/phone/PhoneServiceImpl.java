package kz.alfabank.alfaordersbpm.domain.service.phone;

import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.dto.PersonDetailsDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.*;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.phone.PhoneType;
import kz.alfabank.alfaordersbpm.domain.repositories.PhoneRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.commonservice.CommonServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class PhoneServiceImpl implements PhoneService {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final PhoneRepository phoneRepository;
    private final CommonServiceProxy commonServiceProxy;
    private final BpmCrmProperties bpmCrmProperties;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository, CommonServiceProxy commonServiceProxy, BpmCrmProperties bpmCrmProperties) {
        this.phoneRepository = phoneRepository;
        this.commonServiceProxy = commonServiceProxy;
        this.bpmCrmProperties = bpmCrmProperties;
    }


    @Override
    public List<Phone> getByOrderId(Long orderId) {
        LOG.debug("PhoneService getByOrderId orderID={}", orderId);
        if (orderId == null)
            throw new IllegalArgumentException("PhoneService->getByOrderId orderId is null");

        return phoneRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional(readOnly = true,timeout = 10)
    public Optional<Phone> getOne(Long id) {
        LOG.debug("PhoneService getOne ID={}", id);
        return phoneRepository.findById(id);
    }

    @Override
    public Phone save(Phone entity) {
        LOG.debug("PhoneService save {}", entity);
        return phoneRepository.saveAndFlush(entity);
    }

    @Override
    public Phone update(Phone entity) {
        LOG.debug("PhoneService update {}", entity);
        Long id = entity.getId();
        if(id == null)
            throw new BadRequestException(String.format("To update, Phone must have an ID, Phone=%s", entity.toString()));
        if(!existsById(id))
            throw new EntityNotFoundException(String.format("Phone with ID=%s not found to perform update, Phone=%s", id, entity.toString()));
        return save(entity);
    }

    @Override
    public Phone insert(Phone entity) {
        LOG.debug("PhoneService insert {}", entity);
        Long id = entity.getId();
        if(id != null)
            throw new BadRequestException(String.format("To perform insert, Phone ID must be null, passed Phone=%s", entity.toString()));
        return save(entity);
    }

    @Override
    public Phone fromPersonDetails(Long orderId, PersonDetailsDTO personDetailsDTO) {
        if (orderId == null)
            throw new IllegalArgumentException("PhoneService->fromPersonDetails orderId is null");
        if (personDetailsDTO == null)
            throw new IllegalArgumentException("PhoneService->fromPersonDetails personDetailsDTO is null");

        String phoneDictName = bpmCrmProperties.getDictionaries().getDefaultValues().getPhoneTypeDictName();
        String mobileTypeCode = bpmCrmProperties.getDictionaries().getDefaultValues().getMobilePhoneCode();
        CompletableFuture<Optional<CommonServiceRef>> refFuture = commonServiceProxy.getDictValByCode(phoneDictName, mobileTypeCode);

        Phone phone = new Phone();
        phone.setOrderId(orderId);
        phone.setPhoneType(PhoneType.MOBILE);
        phone.setPhone(personDetailsDTO.getMobilePhone());

        Optional<CommonServiceRef> ref;
        try {
            ref = refFuture.get();
        } catch (Exception e) {
            throw new InternalServerException("PhoneService->fromPersonDetails calling get CommonService Result " + e.getMessage(), e);
        }

        if (!ref.isPresent())
            throw new CommonServiceException(String.format("PhoneService->fromPersonDetails value with code [%s] not found in dictionary %s",mobileTypeCode,phoneDictName));

        phone.setPhoneTypeRef(ref.get());

        return save(phone);
    }

    @Override
    @Transactional(readOnly = true,timeout = 15)
    public boolean existsById(Long id) {
        LOG.debug("PhoneService existsById ID={}", id);
        return phoneRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("PhoneService delete by ID={}", id);
        phoneRepository.deleteById(id);
    }

    @Override
    public List<Phone> getByOrdeIdAndPhoneType(Long orderId, PhoneType phoneType) {
        LOG.debug("PhoneService getByOrdeIdAndPhoneType orderID={} PhoneType={}", orderId, phoneType);
        if (orderId == null)
            throw new IllegalArgumentException("PhoneService->getByOrdeIdAndPhoneType orderId is null");
        if (phoneType == null)
            throw new IllegalArgumentException("PhoneService->getByOrdeIdAndPhoneType phoneType is null");

        return phoneRepository.findByOrderIdAndPhoneType(orderId,phoneType);
    }

}
