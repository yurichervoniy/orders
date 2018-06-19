package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.scoring.Scoring;
import kz.alfabank.alfaordersbpm.domain.models.scoring.ScoringInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScoringRepository extends JpaRepository<Scoring, Long> {

    @Transactional(timeout = 15, readOnly = true)
    List<ScoringInfo> findAllByOrderIdAndActiveTrueOrderById(Long orderId);

    @Transactional(timeout = 20, readOnly = true)
    @Query(value = "select s.REQUEST_BLOB from ORDERS_EXP_SCORING s where s.id=?1", nativeQuery = true)
    byte[] getRequestBlob(Long id);

    @Transactional(timeout = 20, readOnly = true)
    @Query(value = "select s.RESPONSE_BLOB from ORDERS_EXP_SCORING s where s.id=?1", nativeQuery = true)
    byte[] getResponseBlob(Long id);

}
