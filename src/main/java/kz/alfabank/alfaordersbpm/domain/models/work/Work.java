package kz.alfabank.alfaordersbpm.domain.models.work;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "order_work"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id", "organization_name"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class Work extends AuditInfo{

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_gen")
    @SequenceGenerator(name = "work_gen", sequenceName = "work_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Наименование организации", required = true)
    @NotNull(message="Наименование организации не может быть пустым")
    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @ApiModelProperty(notes="Официальная занятость", required = true)
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "offemp_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "offemp_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "offemp_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "offemp_name_lang", nullable = false))
    })
    private CommonServiceRef officialEmpRef;

    @ApiModelProperty(notes="Должность", required = true)
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "position_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "position_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "position_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "position_name_lang", nullable = false))
    })
    private CommonServiceRef positionNameRef;

    @ApiModelProperty(notes="Отрасль деятельности", required = true)
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "indtype_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "indtype_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "indtype_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "indtype_name_lang", nullable = false))
    })
    private CommonServiceRef industryTypeRef;

    @ApiModelProperty(notes="Заработная плата", required = true)
    @NotNull(message="Заработная плата не может быть пустой")
    @Min(value = 0, message = "Заработная плата дожна быть больше либо равна нулю")
    @Column(name = "salary", nullable = false)
    private BigDecimal salarySum;

    @ApiModelProperty(notes = "Продолжительность работы в месяцах", required = true)
    @NotNull(message = "Продолжительность работы не может быть пустой")
    @Column(name = "work_duration", nullable = false)
    private Integer workDuration;

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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public CommonServiceRef getPositionNameRef() {
        return positionNameRef;
    }

    public void setPositionNameRef(CommonServiceRef positionNameRef) {
        this.positionNameRef = positionNameRef;
    }

    public CommonServiceRef getIndustryTypeRef() {
        return industryTypeRef;
    }

    public void setIndustryTypeRef(CommonServiceRef industryTypeRef) {
        this.industryTypeRef = industryTypeRef;
    }

    public BigDecimal getSalarySum() {
        return salarySum;
    }

    public void setSalarySum(BigDecimal salarySum) {
        this.salarySum = salarySum;
    }

    public CommonServiceRef getOfficialEmpRef() {
        return officialEmpRef;
    }

    public void setOfficialEmpRef(CommonServiceRef officialEmpRef) {
        this.officialEmpRef = officialEmpRef;
    }

    public Integer getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Integer workDuration) {
        this.workDuration = workDuration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Work{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", organizationName='").append(organizationName).append('\'');
        sb.append(", officialEmpRef=").append(officialEmpRef);
        sb.append(", positionNameRef=").append(positionNameRef);
        sb.append(", industryTypeRef=").append(industryTypeRef);
        sb.append(", salarySum=").append(salarySum);
        sb.append(", workDuration=").append(workDuration);
        sb.append('}');
        return sb.toString();
    }
}
