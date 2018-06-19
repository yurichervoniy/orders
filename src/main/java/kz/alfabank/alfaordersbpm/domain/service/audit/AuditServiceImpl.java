package kz.alfabank.alfaordersbpm.domain.service.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.alfabank.alfaordersbpm.components.MessageParser;
import kz.alfabank.alfaordersbpm.config.AuditLogProperties;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditLog;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class AuditServiceImpl implements AuditService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditServiceImpl.class);

    private final MessageParser messageParser;
    private final RabbitTemplate rabbitTemplate;
    private final AuditLogProperties auditLogProperties;


    @Autowired
    public AuditServiceImpl(MessageParser messageParser, RabbitTemplate rabbitTemplate, AuditLogProperties auditLogProperties) {
        this.messageParser = messageParser;
        this.rabbitTemplate = rabbitTemplate;
        this.auditLogProperties = auditLogProperties;
    }

    @Override
    public void audit(boolean isSuccess, Auditable auditable, String methodName, Object[] methodArgs, Object result) throws JsonProcessingException {
        LOG.debug("Performing audit {}", auditable);
        AuditLog log = new AuditLog();
        log.setSuccess(isSuccess);
        log.setEventName(auditable.eventName());
        log.setUsername(getCurrentUser());
        log.setMethodName(methodName);
        log.setIpAddress(RequestUtil.getClientIpAddress());
        log.setMachineName(RequestUtil.getClientHostName());
        if (methodArgs != null)
            log.setMethodArgs(Arrays.toString(methodArgs));
        if (result != null)
            log.setResult(result.toString());
        try {
            LOG.debug("Created auditLog message {}", log);
            Message replyMessage = messageParser.createMessage(log);
            rabbitTemplate.send(auditLogProperties.getAuditTopic(), "", replyMessage);
            LOG.debug("send AuditLog message exchange {} data={}", auditLogProperties.getAuditTopic(), replyMessage);
        }catch (JsonProcessingException ex) {
            LOG.error("Error in auditLog " + auditable, ex);
        }
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}

