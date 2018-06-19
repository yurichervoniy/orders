package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Objects;

@ApiModel(description = "Место работы клиента для служб")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkModel {
    @ApiModelProperty(notes="ID записи") private Long id;

    @ApiModelProperty(notes="Наименование организации")
    private String organizationName;

    @ApiModelProperty(notes="Заработная плата клиента")
    private BigDecimal salarySum;

    @ApiModelProperty(notes="Должность клиента")
    private DictionaryModel positionNameRef;

    @JsonCreator
    public WorkModel(@JsonProperty("id") Long id,
                     @JsonProperty("organizationName") String organizationName,
                     @JsonProperty("salarySum") BigDecimal salarySum,
                     @JsonProperty("positionNameRef") DictionaryModel positionNameRef) {
        this.id = id;
        this.organizationName = organizationName;
        this.salarySum = salarySum;
        this.positionNameRef = positionNameRef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public BigDecimal getSalarySum() {
        return salarySum;
    }

    public void setSalarySum(BigDecimal salarySum) {
        this.salarySum = salarySum;
    }

    public DictionaryModel getPositionNameRef() {
        return positionNameRef;
    }

    public void setPositionNameRef(DictionaryModel positionNameRef) {
        this.positionNameRef = positionNameRef;
    }

    @Override
    public String toString() {
        return "WorkModel{" +
                "id=" + id +
                ", organizationName='" + organizationName + '\'' +
                ", salarySum=" + salarySum +
                ", positionNameRef=" + positionNameRef +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkModel workModel = (WorkModel) o;
        return Objects.equals(id, workModel.id) &&
                Objects.equals(organizationName, workModel.organizationName) &&
                Objects.equals(salarySum, workModel.salarySum) &&
                Objects.equals(positionNameRef, workModel.positionNameRef);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, organizationName, salarySum, positionNameRef);
    }
}
