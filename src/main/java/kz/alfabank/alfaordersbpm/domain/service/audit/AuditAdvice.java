package kz.alfabank.alfaordersbpm.domain.service.audit;

import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(AuditAdvice.class);

    @Autowired
    AuditService auditService;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint point, Auditable auditable) throws Throwable {

        boolean ok = false;
        Object o = null;
        try {
            o = point.proceed();
            ok = true;
            return o;
        }
        catch(Exception ex){
            o = ex;
            throw ex;
        }
        finally {
            try {
                auditService.audit(ok, auditable, point.getSignature().getName(), point.getArgs(), o);
            }
            catch (Exception ex){
                LOG.error("Error in auditService.audit " + auditable, ex);
            }
        }
    }

}
