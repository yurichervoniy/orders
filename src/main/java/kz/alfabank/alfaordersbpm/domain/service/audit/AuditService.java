package kz.alfabank.alfaordersbpm.domain.service.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;

public interface AuditService {

    void audit(boolean isSuccess, Auditable auditable, String methodName, Object[] methodArgs, Object result) throws JsonProcessingException;

}
