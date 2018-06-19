package kz.alfabank.alfaordersbpm.domain.models.scoring;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.RowId;
import javax.persistence.*;


@Entity
@Table(name = "ORDERS_EXP_SCORING_RULES")
@RowId("ROWID")
public class ScoringRule {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    @ApiModelProperty(notes="Идентификатор скоринга", required = true)
    @Column(name = "scoring_id", nullable = false, updatable = false)
    private Long scoringId;

    @ApiModelProperty(notes="reason code")
    @Column(name = "reason", length = 1000, updatable = false)
    private String reason;

    @ApiModelProperty(notes="decision")
    @Column(name = "decision", length = 1000, updatable = false)
    private String decision;

    @ApiModelProperty(notes="admintext")
    @Column(name = "admintext", length = 1000, updatable = false)
    private String adminText;

    @ApiModelProperty(notes="managertext")
    @Column(name = "managertext", length = 1000, updatable = false)
    private String managerText;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScoringId() {
        return scoringId;
    }

    public void setScoringId(Long scoringId) {
        this.scoringId = scoringId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getAdminText() {
        return adminText;
    }

    public void setAdminText(String adminText) {
        this.adminText = adminText;
    }

    public String getManagerText() {
        return managerText;
    }

    public void setManagerText(String managerText) {
        this.managerText = managerText;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScoringRule{");
        sb.append("id=").append(id);
        sb.append(", scoringId=").append(scoringId);
        sb.append(", reason='").append(reason).append('\'');
        sb.append(", decision='").append(decision).append('\'');
        sb.append(", adminText='").append(adminText).append('\'');
        sb.append(", managerText='").append(managerText).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
