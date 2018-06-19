package kz.alfabank.alfaordersbpm.domain.models.scoring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "ORDERS_EXP_SCORING")
@RowId("ROWID")
@JsonPropertyOrder({"id", "orderId", "scoringType", "dateInsert", "active", "status", "decisionText", "DECISION_CATEGORY",
   "decisionSetterId", "decisionSetterName", "scoringRules"
})
public class Scoring {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Тип скоринга")
    @Column(name = "SCORING_TYPE", length = 1000, updatable = false)
    private String scoringType;

    @ApiModelProperty(notes="Дата вставки")
    @Column(name = "DATE_INSERT", updatable = false)
    private LocalDateTime dateInsert;

    @ApiModelProperty(notes="Признак активной записи")
    @Column(name = "IS_ACTIVE", updatable = false)
    private Boolean active;

    @ApiModelProperty(notes="Статус")
    @Column(name = "STATUS", length = 1000, updatable = false)
    private String status;

    @ApiModelProperty(notes="Решение скоринга")
    @Column(name = "DECISION_TEXT", length = 1000, updatable = false)
    private String decisionText;

    @ApiModelProperty(notes="Категория решение скоринга")
    @Column(name = "DECISION_CATEGORY", length = 1000, updatable = false)
    private String decisionCategory;

    @ApiModelProperty(notes="DECISION_SETTERID")
    @Column(name = "DECISION_SETTERID", length = 1000, updatable = false)
    private String decisionSetterId;

    @ApiModelProperty(notes="DECISION_SETTERNAME")
    @Column(name = "DECISION_SETTERNAME", length = 1000, updatable = false)
    private String decisionSetterName;

    @ApiModelProperty(notes="Правила скоринга")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="scoring_id")
    private List<ScoringRule> scoringRules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public AbstractOrder getOrder() {
        return order;
    }

    public void setOrder(AbstractOrder order) {
        this.order = order;
    }

    public String getScoringType() {
        return scoringType;
    }

    public void setScoringType(String scoringType) {
        this.scoringType = scoringType;
    }

    public LocalDateTime getDateInsert() {
        return dateInsert;
    }

    public void setDateInsert(LocalDateTime dateInsert) {
        this.dateInsert = dateInsert;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    public String getDecisionCategory() {
        return decisionCategory;
    }

    public void setDecisionCategory(String decisionCategory) {
        this.decisionCategory = decisionCategory;
    }

    public String getDecisionSetterId() {
        return decisionSetterId;
    }

    public void setDecisionSetterId(String decisionSetterId) {
        this.decisionSetterId = decisionSetterId;
    }

    public String getDecisionSetterName() {
        return decisionSetterName;
    }

    public void setDecisionSetterName(String decisionSetterName) {
        this.decisionSetterName = decisionSetterName;
    }

    public List<ScoringRule> getScoringRules() {
        return scoringRules;
    }

    public void setScoringRules(List<ScoringRule> scoringRules) {
        this.scoringRules = scoringRules;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Scoring{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", order=").append(order);
        sb.append(", scoringType='").append(scoringType).append('\'');
        sb.append(", dateInsert=").append(dateInsert);
        sb.append(", active=").append(active);
        sb.append(", status='").append(status).append('\'');
        sb.append(", decisionText='").append(decisionText).append('\'');
        sb.append(", decisionCategory='").append(decisionCategory).append('\'');
        sb.append(", decisionSetterId='").append(decisionSetterId).append('\'');
        sb.append(", decisionSetterName='").append(decisionSetterName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
