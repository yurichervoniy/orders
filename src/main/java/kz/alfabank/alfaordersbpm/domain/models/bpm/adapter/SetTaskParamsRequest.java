package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SetTaskParamsRequest {


    @JsonProperty("data")
    private final TaskResult taskResult;

    public SetTaskParamsRequest(TaskResult taskResult) {
        this.taskResult = taskResult;
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SetTaskParamsRequest{");
        sb.append("taskResult=").append(taskResult);
        sb.append('}');
        return sb.toString();
    }
}
