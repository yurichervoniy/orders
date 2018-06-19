package kz.alfabank.alfaordersbpm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "auditlog", ignoreUnknownFields = false)
@Validated
public class AuditLogProperties {

    @NotBlank
    private String auditTopic;

    public String getAuditTopic() {
        return auditTopic;
    }

    public void setAuditTopic(String auditTopic) {
        this.auditTopic = auditTopic;
    }
}
