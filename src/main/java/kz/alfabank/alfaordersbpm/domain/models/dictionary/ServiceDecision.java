package kz.alfabank.alfaordersbpm.domain.models.dictionary;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ref_service_decision"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"decision_type", "decision_code", "decision_name"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class ServiceDecision extends AuditInfo{

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_dict_gen")
    @SequenceGenerator(name = "service_dict_gen", sequenceName = "service_dict_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Тип службы как ENUM", required = true, allowableValues = "VERIFIER, SECURITY, CREDITADMIN, RISK")
    @NotNull(message = "Тип решения не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "decision_type", nullable = false, length = 50)
    private ServiceDecisionType decisionType;

    @ApiModelProperty(notes="Решение службы", required = true)
    @NotNull(message = "Решение службы не может быть пустым")
    @Column(name = "decision_name", nullable = false, length = 50)
    private String decisionName;

    @ApiModelProperty(notes="Код решения службы", required = true)
    @NotNull(message = "Код решения не может быть пустым")
    @Column(name = "decision_code", nullable = false, length = 50)
    private String decisionCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceDecisionType getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(ServiceDecisionType decisionType) {
        this.decisionType = decisionType;
    }

    public String getDecisionName() {
        return decisionName;
    }

    public void setDecisionName(String decisionName) {
        this.decisionName = decisionName;
    }

    public String getDecisionCode() {
        return decisionCode;
    }

    public void setDecisionCode(String decisionCode) {
        this.decisionCode = decisionCode;
    }

    @Override
    public String toString() {
        return "ServiceDecision{" +
                "id=" + id +
                ", decisionType='" + decisionType + '\'' +
                ", decisionCode='" + decisionCode+ '\'' +
                ", decisionName='" + decisionName + '\'' +
                '}';
    }
}
