package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.approval.FileCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface FileCorrectionRepository extends JpaRepository<FileCorrection, Long> {

    @Transactional(readOnly = true, timeout = 15)
    @Query(value = "SELECT LAST_MODIFIED FROM (SELECT f.*, max(f.ID) OVER () as max_pk FROM order_files_correction f JOIN ORDER_ATTACHMENT a ON a.ID = f.ATTACH_ID  WHERE a.ORDER_ID = ?1 AND a.ATTACHMENT_TYPE = ?2) WHERE id = max_pk", nativeQuery = true)
    LocalDateTime getLastForCorrect(Long orderId, String attachmentType);
}
