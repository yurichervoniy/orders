package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskInfoResponse extends DefaultBpmAdapterResponse {

    @JsonProperty(value = "data")
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
