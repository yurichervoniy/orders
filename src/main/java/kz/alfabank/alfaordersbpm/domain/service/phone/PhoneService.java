package kz.alfabank.alfaordersbpm.domain.service.phone;

import kz.alfabank.alfaordersbpm.domain.models.dto.PersonDetailsDTO;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.phone.PhoneType;

import java.util.List;
import java.util.Optional;

public interface PhoneService {

    List<Phone> getByOrderId(Long orderId);

    Optional<Phone> getOne(Long id);

    Phone save(Phone entity);

    Phone update(Phone entity);

    Phone insert(Phone entity);

    Phone fromPersonDetails(Long orderId,PersonDetailsDTO personDetailsDTO);

    boolean existsById(Long id);

    void delete(Long id);

    List<Phone> getByOrdeIdAndPhoneType(Long orderId, PhoneType phoneType);

}
