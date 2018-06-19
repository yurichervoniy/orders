package kz.alfabank.alfaordersbpm.domain.models.mappers;

import kz.alfabank.alfaordersbpm.domain.models.additionalinfo.AdditionalInfo;
import kz.alfabank.alfaordersbpm.domain.models.address.Address;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressType;
import kz.alfabank.alfaordersbpm.domain.models.contactperson.ContactPerson;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheck;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecision;
import kz.alfabank.alfaordersbpm.domain.models.dto.*;
import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocument;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWeb;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrder;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.signfinal.SignFinal;
import kz.alfabank.alfaordersbpm.domain.models.work.Work;

import java.util.List;

public interface OrderMapper {

    RetailOrder condtionDTOToPilOrder(PilOrderConditionDTO condition);

    void mapPersonDetailsToPilOrder(RetailOrder order, PersonDetailsDTO personDetailsDTO);

    Address personDetailsToAddress(PersonDetailsDTO personDetails);

    IdentityDocument fromDocumentDTO(IdentityDocumentDTO documentDTO);

    Address fromAddressDTO(AddressDTO addressDTO);

    Work fromWorkDetails(WorkDetailsDTO workDetailsDTO);

    Address fromWorkDetailsToAddress(WorkDetailsDTO workDetailsDTO);

    Phone fromWorkDetailsToPhone(WorkDetailsDTO workDetailsDTO);

    String getCrmCodeForAddressType(AddressType addressType);

    AdditionalInfo fromAddInfoDTO(Long orderId, AdditionalInfoDTO infoDTO);

    List<ContactPerson> getContactPersonsFromAddInfoDTO(Long orderId, AdditionalInfoDTO infoDTO);

    List<DictValWeb> getDictValsFromServiceDecision(List<ServiceDecision> decisions);

    List<DictValWeb> getDictValsFromServiceChecks(List<ServiceCheck> checks);

    SignFinal fromSignFinalDTO(SignFinalDTO signFinalDTO);
}
