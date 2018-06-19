package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.phoneconfirm.SmsCodeConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCodeConfirmationRepository extends JpaRepository<SmsCodeConfirmation, Long> {

    Optional<SmsCodeConfirmation> findByPhoneAndOrderId(String phone, Long orderId);

}
