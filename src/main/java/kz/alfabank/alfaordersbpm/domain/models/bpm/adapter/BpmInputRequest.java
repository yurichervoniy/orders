package kz.alfabank.alfaordersbpm.domain.models.bpm.adapter;

public final class BpmInputRequest {

    private final String orderId;
    private final String serviceCode;
    private final String processId;
    private final String correlationId;
    private final String requestId;

    public BpmInputRequest(String orderId, String serviceCode, String processId, String correlationId, String requestId) {
        this.orderId = orderId;
        this.serviceCode = serviceCode;
        this.processId = processId;
        this.correlationId = correlationId;
        this.requestId = requestId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getProcessId() {
        return processId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BpmInputRequest{");
        sb.append("orderId='").append(orderId).append('\'');
        sb.append(", serviceCode='").append(serviceCode).append('\'');
        sb.append(", processId='").append(processId).append('\'');
        sb.append(", correlationId='").append(correlationId).append('\'');
        sb.append(", requestId='").append(requestId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
