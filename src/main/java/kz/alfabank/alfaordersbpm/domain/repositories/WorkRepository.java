package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.work.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {

    Optional<Work> findByOrderId(Long orderId);

}
