package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.signfinal.SignFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SignFinalRepository extends JpaRepository<SignFinal, Long> {
    Optional<SignFinal> findByOrderId(Long orderId);

    @Transactional(readOnly = true, timeout = 15)
    @Query(value = "select client_segment from order_approved_conditions where order_id=?1", nativeQuery = true)
    String getClientSegment(Long orderId);
}
