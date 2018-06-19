package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessData {

    public static final String STEP_UI = "step_ui";

    @JsonProperty(value = "piid")
    private String piid;

    @JsonProperty(value = "state")
    private String state;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "processAppName")
    private String processAppName;

    @JsonProperty(value = "creationTime")
    private LocalDateTime creationTime;

    @JsonProperty(value = "processAppAcronym")
    private String processAppAcronym;

    @JsonProperty(value = "executionState")
    private String executionState;

    @JsonProperty(value = "tasks")
    private List<Task> tasks;

    @JsonProperty(value = "variables")
    private Map<String, Object> variables;

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessAppName() {
        return processAppName;
    }

    public void setProcessAppName(String processAppName) {
        this.processAppName = processAppName;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getProcessAppAcronym() {
        return processAppAcronym;
    }

    public void setProcessAppAcronym(String processAppAcronym) {
        this.processAppAcronym = processAppAcronym;
    }

    public String getExecutionState() {
        return executionState;
    }

    public void setExecutionState(String executionState) {
        this.executionState = executionState;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProcessData{");
        sb.append("piid='").append(piid).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", processAppName='").append(processAppName).append('\'');
        sb.append(", creationTime=").append(creationTime);
        sb.append(", processAppAcronym='").append(processAppAcronym).append('\'');
        sb.append(", executionState='").append(executionState).append('\'');
        sb.append(", tasks=").append(tasks);
        sb.append(", variables=").append(variables);
        sb.append('}');
        return sb.toString();
    }
}
