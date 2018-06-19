package kz.alfabank.alfaordersbpm.domain.models.dictionary;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ref_service_checks"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"result_ckeck_type", "result_check_name", "result_check_code"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class ServiceCheck extends AuditInfo{

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serv_check_dict_gen")
    @SequenceGenerator(name = "serv_check_dict_gen", sequenceName = "serv_check_dict_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Тип проверки как ENUM", required = true, allowableValues = "ADDINFO, NEGATIVEINFO, IDNDOC, JUDGE, ADDRESS")
    @NotNull(message = "Тип проверки не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "result_ckeck_type", nullable = false, length = 50)
    private ServiceCheckType checkType;

    @ApiModelProperty(notes="Результат проверки службы", required = true)
    @NotNull(message = "Решение службы не может быть пустым")
    @Column(name = "result_check_name", nullable = false, length = 50)
    private String checkName;

    @ApiModelProperty(notes="Код результата проверки службы", required = true)
    @NotNull(message = "Код результата проверки не может быть пустым")
    @Column(name = "result_check_code", nullable = false, length = 50)
    private String checkCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceCheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(ServiceCheckType checkType) {
        this.checkType = checkType;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public String toString() {
        return "ServiceCheck{" +
                "id=" + id +
                ", checkType='" + checkType + '\'' +
                ", checkCode='" + checkCode + '\'' +
                ", checkName='" + checkName + '\'' +
                '}';
    }
}
