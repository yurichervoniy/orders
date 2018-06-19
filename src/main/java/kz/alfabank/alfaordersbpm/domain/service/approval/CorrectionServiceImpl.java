package kz.alfabank.alfaordersbpm.domain.service.approval;

import kz.alfabank.alfaordersbpm.domain.models.address.Address;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressType;
import kz.alfabank.alfaordersbpm.domain.models.approval.*;
import kz.alfabank.alfaordersbpm.domain.models.attachment.Attachment;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.contactperson.ContactPerson;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocument;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrder;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.phone.PhoneType;
import kz.alfabank.alfaordersbpm.domain.models.work.Work;
import kz.alfabank.alfaordersbpm.domain.repositories.AttachmentRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.FileCorrectionRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.address.AddressService;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import kz.alfabank.alfaordersbpm.domain.service.contactperson.ContactPersonService;
import kz.alfabank.alfaordersbpm.domain.service.identitydocument.IdentityDocumentService;
import kz.alfabank.alfaordersbpm.domain.service.order.RetailOrderService;
import kz.alfabank.alfaordersbpm.domain.service.phone.PhoneService;
import kz.alfabank.alfaordersbpm.domain.service.work.WorkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(timeout = 60, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class CorrectionServiceImpl implements CorrectionService{
    private static final Logger LOG = LoggerFactory.getLogger(CorrectionServiceImpl.class);
    private static final String NOT_FOUND = "Не удалось найти запись";

    private final AddressService addressService;
    private final IdentityDocumentService identityDocumentService;
    private final RetailOrderService retailOrderService;
    private final PhoneService phoneService;
    private final WorkService workService;
    private final ContactPersonService contactPersonService;
    private final BpmAdapterProxy bpmAdapterProxy;
    private final FileCorrectionRepository fileCorrectionRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    private RetailOrderRepository retailOrderRepository;

    @Autowired
    public CorrectionServiceImpl(AddressService addressService, IdentityDocumentService identityDocumentService, RetailOrderService retailOrderService, PhoneService phoneService, WorkService workService, ContactPersonService contactPersonService, BpmAdapterProxy bpmAdapterProxy, FileCorrectionRepository fileCorrectionRepository, AttachmentRepository attachmentRepository) {
        this.addressService = addressService;
        this.identityDocumentService = identityDocumentService;
        this.retailOrderService = retailOrderService;
        this.phoneService = phoneService;
        this.workService = workService;
        this.contactPersonService = contactPersonService;
        this.bpmAdapterProxy = bpmAdapterProxy;
        this.fileCorrectionRepository = fileCorrectionRepository;
        this.attachmentRepository = attachmentRepository;
    }

    private void mergeDocument(IdentityDocument document, DocumentModel value) {
        if (value.getDocumentTypeRef() != null)
            document.setDocumentTypeRef(CommonServiceRef.of(
                    value.getDocumentTypeRef().getValue(),
                    value.getDocumentTypeRef().getText(),
                    value.getDocumentTypeRef().getDictName()));

        if (value.getDocumentNumber() != null)
            document.setDocumentNumber(value.getDocumentNumber());

        if (value.getDocumentSeries() != null)
            document.setDocumentSeries(value.getDocumentSeries());

        if (value.getIssueAuthorityRef() != null)
            document.setIssueAuthorityRef(CommonServiceRef.of(
                    value.getIssueAuthorityRef().getValue(),
                    value.getIssueAuthorityRef().getText(),
                    value.getIssueAuthorityRef().getDictName()));

        if (value.getIssueDate() != null)
            document.setIssueDate(value.getIssueDate());

        if (value.getExpirationDate() != null)
            document.setExpirationDate(value.getExpirationDate());

        if (value.getCountryRef() != null)
            document.setCountryRef(CommonServiceRef.of(
                    value.getCountryRef().getValue(),
                    value.getCountryRef().getText(),
                    value.getCountryRef().getDictName()));
    }

    private void updateDocument(DocumentModel value) {
        LOG.info("updateDocument {}", value);
        try {
            Optional<IdentityDocument> entity = identityDocumentService.getOne(value.getId());
            if (entity.isPresent()) {
                IdentityDocument document = entity.get();

                if (document.getId() == null)
                    throw new BadRequestException(NOT_FOUND);

                mergeDocument(document, value);

                identityDocumentService.update(document);
            } else {
                throw new BadRequestException(NOT_FOUND);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка обновления типа документа: " + e);
        }
    }

    private void mergeAddress(Address address, AddressModel value) {
        if (value.getDistrictTypeRef() != null)
            address.setDistrictTypeRef(CommonServiceRef.of(
                    value.getDistrictTypeRef().getValue(),
                    value.getDistrictTypeRef().getText(),
                    value.getDistrictTypeRef().getDictName())
            );

        if (value.getFlat() != null)
            address.setFlat(value.getFlat());

        if (value.getHouse() != null)
            address.setHouse(value.getHouse());

        if (value.getMicroDistrict() != null)
            address.setMicroDistrict(value.getMicroDistrict());

        if (value.getLocalityTypeRef() != null)
            address.setLocalityTypeRef(CommonServiceRef.of(
                    value.getLocalityTypeRef().getValue(),
                    value.getLocalityTypeRef().getText(),
                    value.getLocalityTypeRef().getDictName()));

        if (value.getRegionTypeRef() != null)
            address.setRegionTypeRef(CommonServiceRef.of(
                    value.getRegionTypeRef().getValue(),
                    value.getRegionTypeRef().getText(),
                    value.getRegionTypeRef().getDictName()));

        if (value.getStreet() != null)
            address.setStreet(value.getStreet());

        if (value.getTownTypeRef() != null)
            address.setTownTypeRef(CommonServiceRef.of(
                    value.getTownTypeRef().getValue(),
                    value.getTownTypeRef().getText(),
                    value.getTownTypeRef().getDictName()));
    }

    private void updateAddress(Long orderId, AddressType addressType, AddressModel value) {
        LOG.info("updateAddress {}", value);
        try {
            Optional<Address> entity = addressService.getByOrdeIdAndAddresType(orderId, addressType);
            Address address;
            if (entity.isPresent()) {
                address = entity.get();

                if (address.getId() == null)
                    throw new BadRequestException(NOT_FOUND);

                mergeAddress(address, value);

                addressService.update(address);
            } else {
                throw new BadRequestException(NOT_FOUND);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка обновления адреса: " + e);
        }
    }

    private void updatePhone(Long orderId, PhoneModel value, PhoneType phoneType) {
        LOG.info("updatePhone {}", value);
        try {
            List<Phone> phones = phoneService.getByOrdeIdAndPhoneType(orderId, phoneType);

            for (Phone phone : phones) {
                if (phone.getId().equals(value.getId())) {
                    phone.setPhone(value.getValue());
                    phoneService.update(phone);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка обновления адреса: " + e);
        }
    }

    private void updateWork(Long orderId, WorkModel value) {
        LOG.info("updateWork {}", value);
        try {
            Optional<Work> entity = workService.getByOrderId(orderId);

            if (entity.isPresent()) {
                Work work;
                work = entity.get();

                if (value.getOrganizationName() != null)
                    work.setOrganizationName(value.getOrganizationName());

                if (value.getSalarySum() != null)
                    work.setSalarySum(value.getSalarySum());

                if (value.getPositionNameRef() != null)
                    work.setPositionNameRef(CommonServiceRef.of(
                            value.getPositionNameRef().getValue(),
                            value.getPositionNameRef().getText(),
                            value.getPositionNameRef().getDictName()));

                workService.update(work);
            } else {
                throw new BadRequestException(NOT_FOUND);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка обновления организации: " + e);
        }
    }

    private void updateOrder(Long orderId, OrderModel value) {
        LOG.info("updateOrder {}", value);
        try {
            Optional<RetailOrder> entity = retailOrderService.getOne(orderId);

            if (entity.isPresent()) {
                RetailOrder retailOrder;
                retailOrder = entity.get();

                if (value.getIin() != null)
                    retailOrder.setIin(value.getIin());

                if (value.getLastName() != null)
                    retailOrder.setLastName(value.getLastName());

                if (value.getFirstName() != null)
                    retailOrder.setFirstName(value.getFirstName());

                if (value.getMiddleName() != null)
                    retailOrder.setMiddleName(value.getMiddleName());

                if (value.getBirthDate() != null)
                    retailOrder.setBirthDate(value.getBirthDate());

                retailOrderService.update(retailOrder);
            } else {
                LOG.error("updateOrder: {}", NOT_FOUND);
                throw new BadRequestException(NOT_FOUND);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка обновления заявления: " + e);
        }
    }

    private void updateContactPerson(List<ContactPersonsModel> value) {
        LOG.info("updateContactPerson {}", value);
        for (ContactPersonsModel person : value) {
            Optional<ContactPerson> entity = contactPersonService.getOne(person.getId());

            if (entity.isPresent()) {
                ContactPerson contactPerson;
                contactPerson = entity.get();

                if (person.getPersonName() != null)
                    contactPerson.setPersonName(person.getPersonName());

                if (person.getPhone() != null)
                    contactPerson.setPhone(person.getPhone());

                if (person.getRelationTypeRef() != null)
                    contactPerson.setRelationTypeRef(CommonServiceRef.of(
                            person.getRelationTypeRef().getValue(),
                            person.getRelationTypeRef().getText(),
                            person.getRelationTypeRef().getDictName()));

                contactPersonService.save(contactPerson);
            } else {
                LOG.error("updateContactPerson: {}", NOT_FOUND);
                throw new BadRequestException(NOT_FOUND);
            }
        }
    }

    @Override
    public Boolean updateFields(Long orderId, List<CorrectionModel> fields) {
        LOG.info("Корректировка {}", fields);
        for (CorrectionModel field : fields)
            switch (field.getFieldName()) {
                case "registrationAddress":
                    updateAddress(orderId, AddressType.REGISTRATION, (AddressModel) field.getNewValue());
                    break;
                case "residenceAddress":
                    updateAddress(orderId, AddressType.RESIDENCE, (AddressModel) field.getNewValue());
                    break;
                case "mobilePhone":
                    updatePhone(orderId, (PhoneModel) field.getNewValue(), PhoneType.MOBILE);
                    break;
                case "homePhone":
                    updatePhone(orderId, (PhoneModel) field.getNewValue(), PhoneType.HOME);
                    break;
                case "workPhone":
                    updatePhone(orderId, (PhoneModel) field.getNewValue(), PhoneType.WORK);
                    break;
                case "work":
                    updateWork(orderId, (WorkModel) field.getNewValue());
                    break;
                case "document":
                    updateDocument((DocumentModel) field.getNewValue());
                    break;
                case "order":
                    updateOrder(orderId, (OrderModel) field.getNewValue());
                    break;
                case "workAddress":
                    updateAddress(orderId, AddressType.WORK, (AddressModel) field.getNewValue());
                    break;
                case "contactPersons":
                    updateContactPerson((List<ContactPersonsModel>) field.getNewValue());
                    break;
                default:
            }
        return true;
    }

    @Override
    public boolean isFileModified(Long orderId, AttachmentType attachmentType) {
        LocalDateTime fileCorrection = fileCorrectionRepository.getLastForCorrect(orderId, attachmentType.name());
        if (fileCorrection == null)
            throw new EntityNotFoundException("Не найдена запись корректировки файла");
        Attachment attachment = attachmentRepository.findByOrderIdAndAttachmentType(orderId, attachmentType)
                .orElseThrow(() -> new EntityNotFoundException("Не найден приклепленный документ"));

        return fileCorrection.isAfter(attachment.getModifiedDate());
    }

    @Override
    public void completeCorrection(Long orderId, List<CorrectionModel> fields) {
        String processId = retailOrderRepository.getOrderProcessId(orderId);
        RetailOrderUiStep stepUI = RetailOrderUiStep.UPDATE_INFO;
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, stepUI.getStepName());
        Boolean result = updateFields(orderId, fields);
        if (!result)
            throw new IllegalArgumentException("Ошибка обновления полей");

        retailOrderRepository.setProcessingStepUI(orderId, SecurityContextHolder.getContext().getAuthentication().getName());
        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
    }
}
