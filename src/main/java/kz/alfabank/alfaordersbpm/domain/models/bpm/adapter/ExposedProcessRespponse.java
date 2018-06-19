package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExposedProcessRespponse extends DefaultBpmAdapterResponse {

    @JsonProperty(value = "data")
    private ExposedData exposedData;

    public ExposedData getExposedData() {
        return exposedData;
    }

    public void setExposedData(ExposedData exposedData) {
        this.exposedData = exposedData;
    }
}
