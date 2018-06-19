package kz.alfabank.alfaordersbpm.domain.models.mappers;

import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.additionalinfo.AdditionalInfo;
import kz.alfabank.alfaordersbpm.domain.models.address.Address;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressType;
import kz.alfabank.alfaordersbpm.domain.models.contactperson.ContactPerson;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheck;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecision;
import kz.alfabank.alfaordersbpm.domain.models.dto.*;
import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocument;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWeb;
import kz.alfabank.alfaordersbpm.domain.models.order.*;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.phone.PhoneType;
import kz.alfabank.alfaordersbpm.domain.models.signfinal.SignFinal;
import kz.alfabank.alfaordersbpm.domain.models.work.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class OrderMapperImpl implements OrderMapper {

    private static final String DEFAULT_IIN = "000000000000";
    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.of(1970,01,01);
    private static final Integer DEFAUL_SPECIAL_MARK = 0;
    private final BpmCrmProperties bpmCrmProperties;

    @Autowired
    public OrderMapperImpl(BpmCrmProperties bpmCrmProperties) {
        this.bpmCrmProperties = bpmCrmProperties;
    }

    @Override
    public RetailOrder condtionDTOToPilOrder(PilOrderConditionDTO condition) {
        RetailOrder order = new RetailOrder();
        order.setCreditProductRef(condition.getCreditProductRef());
        order.setRequestedAmount(condition.getRequestedAmount());
        order.setPaymentTypeRef(condition.getPaymentTypeRef());
        order.setPaymentType(PaymentType.parseFromCode(condition.getPaymentTypeRef().getValue()));
        order.setInsuranceRef(condition.getInsuranceRef());
        order.setInsuranceAmount(Optional.ofNullable(condition.getInsuranceAmount()).orElse(BigDecimal.ZERO));
        order.setRegularPayment(condition.getRegularPayment());
        order.setLoanDuration(condition.getLoanDuration());
        order.setLoanPeriod(LoanPeriod.getDefaultPeriod());
        order.setAdditionalIncome(condition.getAdditionalIncome());
        order.setAttractedRef(condition.getAttractedRef());
        order.setInitialPayment(BigDecimal.ZERO);
        order.setOrderDate(LocalDate.now());
        order.setActive(true);
        order.setCancelable(true);
        order.setOverPayment(condition.getOverPayment());
        order.setStarted(false);

        order.setIin(DEFAULT_IIN);
        order.setBirthDate(DEFAULT_BIRTH_DATE);
        order.setGender(Gender.MALE);
        order.setSpecialMark(DEFAUL_SPECIAL_MARK);
        return order;
    }

    @Override
    public void mapPersonDetailsToPilOrder(RetailOrder order, PersonDetailsDTO personDetailsDTO) {
        order.setIin(personDetailsDTO.getIin());
        order.setLastName(personDetailsDTO.getLastName());
        order.setFirstName(personDetailsDTO.getFirstName());
        order.setMiddleName(personDetailsDTO.getMiddleName());
        order.setBirthDate(personDetailsDTO.getBirthDate());
        order.setGenderRef(personDetailsDTO.getGenderRef());
        order.setGender(Gender.parseFromString(personDetailsDTO.getGenderRef().getText()));
        order.setSpecialMark(personDetailsDTO.getSpecialMark());
    }

    @Override
    public Address personDetailsToAddress(PersonDetailsDTO personDetails) {
        Address address = new Address();
        address.setRegionTypeRef(personDetails.getRegionTypeRef());
        address.setDistrictTypeRef(personDetails.getDistrictTypeRef());
        address.setTownTypeRef(personDetails.getTownTypeRef());
        address.setLocalityTypeRef(personDetails.getLocalityTypeRef());
        address.setStreet(personDetails.getStreet());
        address.setMicroDistrict(personDetails.getMicroDistrict());
        address.setHouse(personDetails.getHouse());
        address.setFlat(personDetails.getFlat());
        return address;
    }

    @Override
    public IdentityDocument fromDocumentDTO(IdentityDocumentDTO documentDTO) {
        IdentityDocument document = new IdentityDocument();
        document.setCountryRef(documentDTO.getCountryRef());
        document.setDocumentNumber(documentDTO.getDocumentNumber());
        document.setDocumentSeries(documentDTO.getDocumentSeries());
        document.setDocumentTypeRef(documentDTO.getDocumentTypeRef());
        document.setExpirationDate(documentDTO.getExpirationDate());
        document.setIssueAuthorityRef(documentDTO.getIssueAuthorityRef());
        document.setIssueDate(documentDTO.getIssueDate());
        document.setOrderId(documentDTO.getOrderId());
        return document;
    }

    @Override
    public Address fromAddressDTO(AddressDTO addressDTO) {
        Address address = new Address();
        address.setOrderId(addressDTO.getOrderId());
        address.setAddressType(addressDTO.getAddressType());
        address.setRegionTypeRef(addressDTO.getRegionTypeRef());
        address.setDistrictTypeRef(addressDTO.getDistrictTypeRef());
        address.setTownTypeRef(addressDTO.getTownTypeRef());
        address.setLocalityTypeRef(addressDTO.getLocalityTypeRef());
        address.setStreet(addressDTO.getStreet());
        address.setMicroDistrict(addressDTO.getMicroDistrict());
        address.setHouse(addressDTO.getHouse());
        address.setFlat(addressDTO.getFlat());
        return address;
    }

    @Override
    public Work fromWorkDetails(WorkDetailsDTO workDetailsDTO) {
        Work work = new Work();
        work.setOrganizationName(workDetailsDTO.getOrganizationName());
        work.setPositionNameRef(workDetailsDTO.getPositionNameRef());
        work.setIndustryTypeRef(workDetailsDTO.getIndustryTypeRef());
        work.setSalarySum(workDetailsDTO.getSalarySum());
        work.setOfficialEmpRef(workDetailsDTO.getOfficialEmpRef());
        work.setWorkDuration(workDetailsDTO.getWorkDuration());
        return work;
    }

    @Override
    public Address fromWorkDetailsToAddress(WorkDetailsDTO workDetailsDTO) {
        Address address = new Address();
        address.setRegionTypeRef(workDetailsDTO.getRegionTypeRef());
        address.setDistrictTypeRef(workDetailsDTO.getDistrictTypeRef());
        address.setTownTypeRef(workDetailsDTO.getTownTypeRef());
        address.setLocalityTypeRef(workDetailsDTO.getLocalityTypeRef());
        address.setStreet(workDetailsDTO.getStreet());
        address.setMicroDistrict(workDetailsDTO.getMicroDistrict());
        address.setHouse(workDetailsDTO.getHouse());
        address.setFlat(workDetailsDTO.getFlat());
        address.setAddressType(AddressType.WORK);
        return address;
    }

    @Override
    public Phone fromWorkDetailsToPhone(WorkDetailsDTO workDetailsDTO) {
        Phone phone = new Phone();
        phone.setPhone(workDetailsDTO.getPhone());
        phone.setPhoneType(PhoneType.WORK);
        return phone;
    }

    @Override
    public String getCrmCodeForAddressType(AddressType addressType) {
        String crmCode;
        switch (addressType) {
            case WORK: crmCode = bpmCrmProperties.getDictionaries().getDefaultValues().getWorkAddressCode();
                break;
            case REGISTRATION: crmCode = bpmCrmProperties.getDictionaries().getDefaultValues().getRegistrationAddressCode();
                break;
            case RESIDENCE: crmCode = bpmCrmProperties.getDictionaries().getDefaultValues().getLiveAddressCode();
                break;
            default: crmCode = null;
        }
        Objects.requireNonNull(crmCode, String.format("Could not get crmCode from AddressType=%s", addressType));
        return crmCode;
    }

    @Override
    public AdditionalInfo fromAddInfoDTO(Long orderId, AdditionalInfoDTO infoDTO) {
        AdditionalInfo info = new AdditionalInfo();
        info.setOrderId(orderId);
        info.setOtherBankAccounts(infoDTO.getOtherBankAccounts());
        info.setEducationLevel(infoDTO.getEducationLevelRef());
        info.setMarriageStatus(infoDTO.getMarriageStatusRef());
        info.setTaxResident(infoDTO.getTaxResident());
        info.setTaxResidentCountry(infoDTO.getTaxResidentCountry());
        info.setForeignResidence(infoDTO.getForeignResidence());
        info.setForeignResidenceCountry(infoDTO.getForeignResidenceCountry());
        info.setUsaResidence(infoDTO.getUsaResidence());
        info.setForeignTaxNumber(infoDTO.getForeignTaxNumber());
        info.setSecretWord(infoDTO.getSecretWord());
        return info;
    }

    @Override
    public List<ContactPerson> getContactPersonsFromAddInfoDTO(Long orderId, AdditionalInfoDTO infoDTO) {
        List<ContactPerson> result = new ArrayList<>(2);
        List<ContactPersonDTO> contacts = infoDTO.getContactPersons();
        if (contacts == null || contacts.isEmpty())
            return result;
        for (ContactPersonDTO dto : contacts){
            ContactPerson contactPerson = new ContactPerson();
            contactPerson.setOrderId(orderId);
            contactPerson.setPersonName(dto.getPersonName());
            contactPerson.setPhone(dto.getPhone());
            contactPerson.setRelationTypeRef(dto.getRelationTypeRef());
            result.add(contactPerson);
        }
        return result;
    }

    @Override
    public List<DictValWeb> getDictValsFromServiceDecision(List<ServiceDecision> decisions){
        List<DictValWeb> result = new ArrayList<>();
        if (decisions==null||decisions.isEmpty())
            return result;
        for (ServiceDecision dec : decisions){
            DictValWeb dictValWeb = new DictValWeb();
            dictValWeb.setCode(dec.getDecisionCode());
            dictValWeb.setValue(dec.getDecisionName());
            result.add(dictValWeb);
        }
        return result;
    }

    @Override
    public List<DictValWeb> getDictValsFromServiceChecks(List<ServiceCheck> checks){
        List<DictValWeb> result = new ArrayList<>();
        if (checks==null||checks.isEmpty())
            return result;
        for (ServiceCheck check : checks){
            DictValWeb dictValWeb = new DictValWeb();
            dictValWeb.setCode(check.getCheckCode());
            dictValWeb.setValue(check.getCheckName());
            result.add(dictValWeb);
        }
        return result;
    }

    @Override
    public SignFinal fromSignFinalDTO(SignFinalDTO signFinalDTO) {
        SignFinal signFinal = new SignFinal();
        signFinal.setOrderId(signFinalDTO.getOrderId());
        signFinal.setPayDay(signFinalDTO.getPayDay());
        signFinal.setAccount(signFinalDTO.getAccount());
        signFinal.setCardFl(signFinalDTO.getCardFl());
        signFinal.setCardIDN(signFinalDTO.getCardIDN());
        signFinal.setStatus(signFinalDTO.getStatus());
        signFinal.setSigned(signFinalDTO.getSigned());
        signFinal.setClientSegment(signFinalDTO.getClientSegment());
        return signFinal;
    }
}
