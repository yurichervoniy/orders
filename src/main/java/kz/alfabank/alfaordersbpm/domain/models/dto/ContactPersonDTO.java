package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.Constants;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class ContactPersonDTO {

    @ApiModelProperty(notes="Наименование субъекта", required = true)
    @NotBlank(message = "Наименование субъекта не может быть пустым")
    private String personName;

    @ApiModelProperty(notes="Телефон", required = true)
    @Size(min = Constants.PHONE_LENGTH, max = Constants.PHONE_LENGTH)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Номер телефона не соответсвует regexp-у")
    private String phone;

    @ApiModelProperty(notes="Степень родства", required = true)
    @NotNull(message = "Степень родства не может быть пустой")
    private CommonServiceRef relationTypeRef;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CommonServiceRef getRelationTypeRef() {
        return relationTypeRef;
    }

    public void setRelationTypeRef(CommonServiceRef relationTypeRef) {
        this.relationTypeRef = relationTypeRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactPersonDTO that = (ContactPersonDTO) o;
        return Objects.equals(personName, that.personName) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(relationTypeRef, that.relationTypeRef);
    }

    @Override
    public int hashCode() {

        return Objects.hash(personName, phone, relationTypeRef);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContactPersonDTO{");
        sb.append("personName='").append(personName).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", relationTypeRef=").append(relationTypeRef);
        sb.append('}');
        return sb.toString();
    }
}
