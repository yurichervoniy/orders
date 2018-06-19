package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.phone.PhoneType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    List<Phone> findByOrderId(Long orderId);

    Optional<Phone> findByOrderIdAndPhoneAndPhoneType(Long orderId, String phone, PhoneType phoneType);

    List<Phone> findByOrderIdAndPhone(Long orderId, String phone);

    List<Phone> findByOrderIdAndPhoneType(Long orderId, PhoneType phoneType);

}
