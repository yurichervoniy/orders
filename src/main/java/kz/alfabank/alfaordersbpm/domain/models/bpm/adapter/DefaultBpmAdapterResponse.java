package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultBpmAdapterResponse {

    @JsonProperty(value = "RETCODE")
    private Long retCode;

    @JsonProperty(value = "RETTEXT")
    private String retText;

    @JsonProperty(value = "TARGET_HOST")
    private String targetHost;

    @JsonProperty(value = "status")
    private String status;

    public Long getRetCode() {
        return retCode;
    }

    public void setRetCode(Long retCode) {
        this.retCode = retCode;
    }

    public String getRetText() {
        return retText;
    }

    public void setRetText(String retText) {
        this.retText = retText;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultBpmAdapterResponse{");
        sb.append("retCode='").append(retCode).append('\'');
        sb.append(", retText='").append(retText).append('\'');
        sb.append(", targetHost='").append(targetHost).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
