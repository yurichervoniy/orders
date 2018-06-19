package kz.alfabank.alfaordersbpm.domain.models.bpm.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;

@JsonPropertyOrder({"serviceRequestId", "taskResult","success","status","exceptionMessage","orderId","processId","requestId","correlationId"})
public class NotifyMessage {

    @JacksonXmlProperty(localName = "serviceRequestId")
    private final NotifyData serviceRequestId;
    @JacksonXmlProperty(localName = "taskResult")
    private final NotifyData taskResult;
    @JacksonXmlProperty(localName = "success")
    private final NotifyData success;
    @JacksonXmlProperty(localName = "status")
    private final NotifyData status;
    @JacksonXmlProperty(localName = "exceptionMessage")
    private final NotifyData exceptionMessage;
    @JacksonXmlProperty(localName = "orderId")
    private final NotifyData orderId;
    @JacksonXmlProperty(localName = "processId")
    private final NotifyData processId;
    @JacksonXmlProperty(localName = "requestId")
    private final NotifyData requestId;
    @JacksonXmlProperty(localName = "correlationId")
    private final NotifyData correlationId;

    private NotifyMessage(AbstractOrder order){
        this.serviceRequestId = NotifyData.of(Integer.toString(0));
        this.taskResult = NotifyData.of(order.getStepUI());
        this.success = NotifyData.of(Boolean.TRUE.toString());
        this.status = NotifyData.of(order.getOrderState().name());
        this.exceptionMessage = NotifyData.of(null);
        this.orderId = NotifyData.of(order.getId().toString());
        this.processId = NotifyData.of(order.getPiid());
        this.requestId = NotifyData.of(order.getRequestId().toString());
        this.correlationId = NotifyData.of(order.getCorrelationId());
    }

    public static NotifyMessage of(AbstractOrder order){
        return new NotifyMessage(order);
    }

    public NotifyData getServiceRequestId() {
        return serviceRequestId;
    }

    public NotifyData getTaskResult() {
        return taskResult;
    }

    public NotifyData getSuccess() {
        return success;
    }

    public NotifyData getStatus() {
        return status;
    }

    public NotifyData getExceptionMessage() {
        return exceptionMessage;
    }

    public NotifyData getOrderId() {
        return orderId;
    }

    public NotifyData getProcessId() {
        return processId;
    }

    public NotifyData getRequestId() {
        return requestId;
    }

    public NotifyData getCorrelationId() {
        return correlationId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NotifyMessage{");
        sb.append("serviceRequestId=").append(serviceRequestId);
        sb.append(", taskResult=").append(taskResult);
        sb.append(", success=").append(success);
        sb.append(", status=").append(status);
        sb.append(", exceptionMessage=").append(exceptionMessage);
        sb.append(", orderId=").append(orderId);
        sb.append(", processId=").append(processId);
        sb.append(", requestId=").append(requestId);
        sb.append(", correlationId=").append(correlationId);
        sb.append('}');
        return sb.toString();
    }
}
