package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    public static final String ASSIGNED_TO_GROUP = "group";

    public static final String RECEIVED_STATUS = "Received";

    @JsonProperty(value = "priorityName")
    private String priorityName;

    @JsonProperty(value = "displayName")
    private String displayName;

    @JsonProperty(value = "originator")
    private String originator;

    @JsonProperty(value = "assignedTo")
    private String assignedTo;

    @JsonProperty(value = "completionTime")
    private LocalDateTime completionTime;

    @JsonProperty(value = "assignedToType")
    private String assignedToType;

    @JsonProperty(value = "startTime")
    private LocalDateTime startTime;

    @JsonProperty(value = "state")
    private String state;

    @JsonProperty(value = "activationTime")
    private LocalDateTime activationTime;

    @JsonProperty(value = "assignedToDisplayName")
    private String assignedToDisplayName;

    @JsonProperty(value = "piid")
    private String piid;

    @JsonProperty(value = "owner")
    private String owner;

    @JsonProperty(value = "priority")
    private String priority;

    @JsonProperty(value = "dueTime")
    private LocalDateTime dueTime;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "tkiid")
    private String tkiid;

    @JsonProperty(value = "status")
    private String status;

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public String getAssignedToType() {
        return assignedToType;
    }

    public void setAssignedToType(String assignedToType) {
        this.assignedToType = assignedToType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(LocalDateTime activationTime) {
        this.activationTime = activationTime;
    }

    public String getAssignedToDisplayName() {
        return assignedToDisplayName;
    }

    public void setAssignedToDisplayName(String assignedToDisplayName) {
        this.assignedToDisplayName = assignedToDisplayName;
    }

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTkiid() {
        return tkiid;
    }

    public void setTkiid(String tkiid) {
        this.tkiid = tkiid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("priorityName='").append(priorityName).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", originator='").append(originator).append('\'');
        sb.append(", assignedTo='").append(assignedTo).append('\'');
        sb.append(", completionTime=").append(completionTime);
        sb.append(", assignedToType='").append(assignedToType).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", state='").append(state).append('\'');
        sb.append(", activationTime=").append(activationTime);
        sb.append(", assignedToDisplayName='").append(assignedToDisplayName).append('\'');
        sb.append(", piid='").append(piid).append('\'');
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", priority='").append(priority).append('\'');
        sb.append(", dueTime=").append(dueTime);
        sb.append(", name='").append(name).append('\'');
        sb.append(", tkiid='").append(tkiid).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
