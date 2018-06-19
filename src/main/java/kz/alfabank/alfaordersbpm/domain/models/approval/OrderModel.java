package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.Objects;

@ApiModel(description = "Модель данных по заявке")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderModel {
    @ApiModelProperty(notes="ИИН клиента")
    private String iin;

    @ApiModelProperty(notes="Фамилия клиента")
    private String lastName;

    @ApiModelProperty(notes="Имя клиента")
    private String firstName;

    @ApiModelProperty(notes="Отчество клиента")
    private String middleName;

    @ApiModelProperty(notes="Дата рождения клиента")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    @ApiModelProperty(notes="Пол клиента")
    private DictionaryModel genderRef;

    @JsonCreator
    public OrderModel(@JsonProperty("iin") String iin,
                      @JsonProperty("lastName") String lastName,
                      @JsonProperty("firstName") String firstName,
                      @JsonProperty("middleName") String middleName,
                      @JsonProperty("birthDate") LocalDate birthDate,
                      @JsonProperty("genderRef") DictionaryModel genderRef) {
        this.iin = iin;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.genderRef = genderRef;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public DictionaryModel getGenderRef() {
        return genderRef;
    }

    public void setGenderRef(DictionaryModel genderRef) {
        this.genderRef = genderRef;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "iin='" + iin + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", genderRef=" + genderRef +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderModel that = (OrderModel) o;
        return Objects.equals(iin, that.iin) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(birthDate, that.birthDate) &&
                Objects.equals(genderRef, that.genderRef);
    }

    @Override
    public int hashCode() {

        return Objects.hash(iin, lastName, firstName, middleName, birthDate, genderRef);
    }
}
