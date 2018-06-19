package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.scoring.ApprovedConditions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovedConditionsRepository extends JpaRepository<ApprovedConditions, Long> {

    Optional<ApprovedConditions> findByOrderId (Long orderId);

}
