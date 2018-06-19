package kz.alfabank.alfaordersbpm.domain.models.movement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;


@ApiModel(description = "Движение заявки")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderMovement {

    @ApiModelProperty(notes="Идентификатор заявки")
    private Long orderId;

    @ApiModelProperty(notes="Дата создания записи")
    private LocalDateTime createdDate;

    @ApiModelProperty(notes="Дата начала задачи")
    private LocalDateTime startDate;

    @ApiModelProperty(notes="Дата окончания задачи")
    private LocalDateTime endDate;

    @ApiModelProperty(notes="Наименование шага")
    private String stepName;

    @ApiModelProperty(notes="ИД процесса BPM")
    private String piid;

    @ApiModelProperty(notes="ИД задачи BPM")
    private String taskid;

    @ApiModelProperty(notes = "id кореляциии")
    private String correlationId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderMovement{");
        sb.append(", orderId=").append(orderId);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", stepName='").append(stepName).append('\'');
        sb.append(", piid='").append(piid).append('\'');
        sb.append(", taskid='").append(taskid).append('\'');
        sb.append(", correlationId='").append(correlationId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
