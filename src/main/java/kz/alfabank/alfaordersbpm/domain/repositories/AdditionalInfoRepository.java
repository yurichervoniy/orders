package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.additionalinfo.AdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdditionalInfoRepository extends JpaRepository<AdditionalInfo, Long> {

    Optional<AdditionalInfo> findByOrderId(Long orderId);

}
