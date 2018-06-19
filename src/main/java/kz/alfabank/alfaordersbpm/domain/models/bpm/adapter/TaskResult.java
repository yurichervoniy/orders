package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.BooleanUtils;

public class TaskResult {

    public static final String APPROVE = "APPROVE";

    @JsonProperty("taskId")
    private final String taskId;

    @JsonProperty("step")
    private final String step;

    @JsonProperty("taskResult")
    private final String taskResult;

    @JsonIgnore
    private final boolean success;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("exceptionMessage")
    private final String exceptionMessage;


    private TaskResult(String taskId, String step, String taskResult, boolean success, String status, String exceptionMessage) {
        this.taskId = taskId;
        this.step = step;
        this.taskResult = taskResult;
        this.success = success;
        this.status = status;
        this.exceptionMessage = exceptionMessage;
    }

    public static TaskResult of(String taskId, String step, String taskResult, boolean success, String status, String exceptionMessage){
        return new TaskResult(taskId, step, taskResult, success, status, exceptionMessage);
    }

    public static TaskResult ofSuccess(String taskId, String step, String taskResult, String status){
        return TaskResult.of(taskId, step, taskResult, true, status, null);
    }

    public static TaskResult ofSuccess(String taskId, String step, String taskResult){
        return TaskResult.of(taskId,step, taskResult, true, null, null);
    }

    public static TaskResult ofApprove(String taskId, String step){
        return TaskResult.of(taskId,step, APPROVE, true, null, null);
    }

    public static TaskResult ofApprove(Task task){
        return TaskResult.ofApprove(task.getTkiid(), task.getName());
    }

    public static TaskResult ofException(String taskId, String step, String taskResult, Throwable throwable){
        return TaskResult.of(taskId, step, taskResult, false, "Exception", throwable.getMessage());
    }

    public String getStep() {
        return step;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    @JsonProperty("success")
    public String getSuccess() {
        return BooleanUtils.toStringTrueFalse(success);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaskResult{");
        sb.append("step='").append(step).append('\'');
        sb.append(", taskResult='").append(taskResult).append('\'');
        sb.append(", success=").append(success);
        sb.append(", status='").append(status).append('\'');
        sb.append(", exceptionMessage='").append(exceptionMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
