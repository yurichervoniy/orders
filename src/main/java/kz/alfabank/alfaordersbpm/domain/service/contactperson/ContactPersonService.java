package kz.alfabank.alfaordersbpm.domain.service.contactperson;

import kz.alfabank.alfaordersbpm.domain.models.contactperson.ContactPerson;

import java.util.List;
import java.util.Optional;

public interface ContactPersonService {

    Optional<ContactPerson> getOne(Long id);

    ContactPerson save(ContactPerson entity);

    List<ContactPerson> saveAll(Iterable<ContactPerson> iterable);

    boolean existsById(Long id);

    void delete(Long id);

    List<ContactPerson> getByOrderId(Long orderId);

}
