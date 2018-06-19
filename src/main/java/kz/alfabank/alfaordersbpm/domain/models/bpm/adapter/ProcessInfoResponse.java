package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInfoResponse extends DefaultBpmAdapterResponse {

    @JsonProperty(value = "data")
    private ProcessData processData;

    public ProcessData getProcessData() {
        return processData;
    }

    public void setProcessData(ProcessData processData) {
        this.processData = processData;
    }

}
