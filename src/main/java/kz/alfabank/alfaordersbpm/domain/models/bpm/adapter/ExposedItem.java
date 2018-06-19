package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExposedItem {

    @JsonProperty(value = "processAppName")
    private String processAppName;

    @JsonProperty(value = "processAppAcronym")
    private String processAppAcronym;

    public String getProcessAppName() {
        return processAppName;
    }

    public void setProcessAppName(String processAppName) {
        this.processAppName = processAppName;
    }

    public String getProcessAppAcronym() {
        return processAppAcronym;
    }

    public void setProcessAppAcronym(String processAppAcronym) {
        this.processAppAcronym = processAppAcronym;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExposedItem{");
        sb.append("processAppName='").append(processAppName).append('\'');
        sb.append(", processAppAcronym='").append(processAppAcronym).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
