package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class StartProcessRequest {

    @JsonIgnore
    private final String bpdId;

    @JsonIgnore
    private final String branchId;

    @JsonIgnore
    private final String processAppID;

    @JsonProperty("inputRequest")
    private final BpmInputRequest bpmInputRequest;


    public StartProcessRequest(String bpdId, String branchId, String processAppID, BpmInputRequest bpmInputRequest) {
        this.bpdId = bpdId;
        this.branchId = branchId;
        this.processAppID = processAppID;
        this.bpmInputRequest = bpmInputRequest;
    }

    public String getBpdId() {
        return bpdId;
    }

    public String getBranchId() {
        return branchId;
    }

    public BpmInputRequest getBpmInputRequest() {
        return bpmInputRequest;
    }

    public String getProcessAppID() {
        return processAppID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StartProcessRequest{");
        sb.append("bpdId='").append(bpdId).append('\'');
        sb.append(", branchId='").append(branchId).append('\'');
        sb.append(", processAppID='").append(processAppID).append('\'');
        sb.append(", bpmInputRequest=").append(bpmInputRequest);
        sb.append('}');
        return sb.toString();
    }
}
