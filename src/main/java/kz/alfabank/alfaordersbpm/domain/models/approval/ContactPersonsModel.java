package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactPersonsModel {
    @ApiModelProperty(notes="ID контактного лица")
    private Long id;
    @ApiModelProperty(notes="Имя контактного лица")
    private String personName;
    @ApiModelProperty(notes="Телефон")
    private String phone;
    @ApiModelProperty(notes="Регион")
    private DictionaryModel relationTypeRef;

    @JsonCreator
    public ContactPersonsModel(@JsonProperty("id") Long id,
                               @JsonProperty("personName") String personName,
                               @JsonProperty("phone") String phone,
                               @JsonProperty("relationTypeRef") DictionaryModel relationTypeRef) {
        this.id = id;
        this.personName = personName;
        this.phone = phone;
        this.relationTypeRef = relationTypeRef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public DictionaryModel getRelationTypeRef() {
        return relationTypeRef;
    }

    public void setRelationTypeRef(DictionaryModel relationTypeRef) {
        this.relationTypeRef = relationTypeRef;
    }

    @Override
    public String toString() {
        return "ContactPersonsModel{" +
                "id=" + id +
                ", personName='" + personName + '\'' +
                ", phone='" + phone + '\'' +
                ", relationTypeRef=" + relationTypeRef +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactPersonsModel that = (ContactPersonsModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(personName, that.personName) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(relationTypeRef, that.relationTypeRef);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, personName, phone, relationTypeRef);
    }
}
