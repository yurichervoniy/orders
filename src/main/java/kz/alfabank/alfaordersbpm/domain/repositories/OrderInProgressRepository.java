package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.order.OrderInProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderInProgressRepository extends JpaRepository<OrderInProgress, Long> {

    Optional<OrderInProgress> findByOrderId(Long orderId);

    Optional<OrderInProgress> findByIin(String iin);

    void deleteByOrderId(Long orderId);

}
