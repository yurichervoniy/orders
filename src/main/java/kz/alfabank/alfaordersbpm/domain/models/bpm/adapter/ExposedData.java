package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExposedData {

    @JsonProperty(value = "exposedItemsList")
    private List<ExposedItem> exposedItemsList;

    public List<ExposedItem> getExposedItemsList() {
        return exposedItemsList;
    }

    public void setExposedItemsList(List<ExposedItem> exposedItemsList) {
        this.exposedItemsList = exposedItemsList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExposedData{");
        sb.append("exposedItemsList=").append(exposedItemsList);
        sb.append('}');
        return sb.toString();
    }
}
