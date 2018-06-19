package kz.alfabank.alfaordersbpm.domain.repositories.audit;

import kz.alfabank.alfaordersbpm.domain.models.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
