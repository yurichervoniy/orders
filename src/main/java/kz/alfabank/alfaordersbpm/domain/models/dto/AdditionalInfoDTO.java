package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Validated
public class AdditionalInfoDTO {

    @ApiModelProperty(notes = "Счета в других банках")
    private CommonServiceRef otherBankAccounts;

    @ApiModelProperty(notes="Домашний телефон")
    private String homePhone;

    @ApiModelProperty(notes="Образование", required = true)
    @NotNull(message = "Образование не может быть пустым")
    private CommonServiceRef educationLevelRef;

    @ApiModelProperty(notes = "Семейное положения", required = true)
    @NotNull(message = "Семейное положение не может быть пустым")
    private CommonServiceRef marriageStatusRef;

    @ApiModelProperty(notes = "Контактные лица", required = true)
    @NotNull(message = "Контактные лица не могут быть пустыми")
    @Size(min = 1, message = "Контактных лиц должно быть больше одного")
    @Valid
    private List<ContactPersonDTO> contactPersons;

    @ApiModelProperty(notes="Признак причастия к обязательству уплаты налогов в инотранном государстве", required = true)
    @NotNull(message = "Признак причастия к обязательству уплаты налогов в инотранном государстве не может быть пустым")
    private Boolean taxResident;

    @ApiModelProperty(notes="Страна для налогового резидента")
    private CommonServiceRef taxResidentCountry;

    @ApiModelProperty(notes="Признак наличия иностранного вида на жительство", required = true)
    @NotNull(message = "Признак наличия иностранного вида на жительство не может быть пустым")
    private Boolean foreignResidence;

    @ApiModelProperty(notes="Страна вида на жительства")
    private CommonServiceRef foreignResidenceCountry;

    @ApiModelProperty(notes="Признак пребывания на територии США", required = true)
    @NotNull(message = "Признак пребывания на територии США не может быть пустым")
    private Boolean usaResidence;

    @ApiModelProperty(notes="ИНН иностранного гражданина")
    @Size(max = 100, message = "Длина ИНН не должна первышать 100 символов")
    private String foreignTaxNumber;

    @ApiModelProperty(notes="Кодовое слово")
    @Size(max = 100, message = "Длина кодового слова не должна первышать 100 символов")
    private String secretWord;

    public CommonServiceRef getOtherBankAccounts() {
        return otherBankAccounts;
    }

    public void setOtherBankAccounts(CommonServiceRef otherBankAccounts) {
        this.otherBankAccounts = otherBankAccounts;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public CommonServiceRef getEducationLevelRef() {
        return educationLevelRef;
    }

    public void setEducationLevelRef(CommonServiceRef educationLevelRef) {
        this.educationLevelRef = educationLevelRef;
    }

    public CommonServiceRef getMarriageStatusRef() {
        return marriageStatusRef;
    }

    public void setMarriageStatusRef(CommonServiceRef marriageStatusRef) {
        this.marriageStatusRef = marriageStatusRef;
    }

    public List<ContactPersonDTO> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPersonDTO> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public Boolean getTaxResident() {
        return taxResident;
    }

    public void setTaxResident(Boolean taxResident) {
        this.taxResident = taxResident;
    }

    public CommonServiceRef getTaxResidentCountry() {
        return taxResidentCountry;
    }

    public void setTaxResidentCountry(CommonServiceRef taxResidentCountry) {
        this.taxResidentCountry = taxResidentCountry;
    }

    public Boolean getForeignResidence() {
        return foreignResidence;
    }

    public void setForeignResidence(Boolean foreignResidence) {
        this.foreignResidence = foreignResidence;
    }

    public CommonServiceRef getForeignResidenceCountry() {
        return foreignResidenceCountry;
    }

    public void setForeignResidenceCountry(CommonServiceRef foreignResidenceCountry) {
        this.foreignResidenceCountry = foreignResidenceCountry;
    }

    public Boolean getUsaResidence() {
        return usaResidence;
    }

    public void setUsaResidence(Boolean usaResidence) {
        this.usaResidence = usaResidence;
    }

    public String getForeignTaxNumber() {
        return foreignTaxNumber;
    }

    public void setForeignTaxNumber(String foreignTaxNumber) {
        this.foreignTaxNumber = foreignTaxNumber;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdditionalInfoDTO that = (AdditionalInfoDTO) o;
        return Objects.equals(otherBankAccounts, that.otherBankAccounts) &&
                Objects.equals(homePhone, that.homePhone) &&
                Objects.equals(educationLevelRef, that.educationLevelRef) &&
                Objects.equals(marriageStatusRef, that.marriageStatusRef) &&
                Objects.equals(taxResident, that.taxResident) &&
                Objects.equals(taxResidentCountry, that.taxResidentCountry) &&
                Objects.equals(foreignResidence, that.foreignResidence) &&
                Objects.equals(foreignResidenceCountry, that.foreignResidenceCountry) &&
                Objects.equals(usaResidence, that.usaResidence) &&
                Objects.equals(foreignTaxNumber, that.foreignTaxNumber) &&
                Objects.equals(secretWord, that.secretWord);
    }

    @Override
    public int hashCode() {

        return Objects.hash(otherBankAccounts, homePhone, educationLevelRef, marriageStatusRef, contactPersons, taxResident, taxResidentCountry, foreignResidence, foreignResidenceCountry, usaResidence,foreignTaxNumber,usaResidence);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AdditionalInfoDTO{");
        sb.append("otherBankAccounts=").append(otherBankAccounts);
        sb.append(", homePhone='").append(homePhone).append('\'');
        sb.append(", educationLevelRef=").append(educationLevelRef);
        sb.append(", marriageStatusRef=").append(marriageStatusRef);
        sb.append(", contactPersons=").append(contactPersons);
        sb.append(", taxResident=").append(taxResident);
        sb.append(", taxResidentCountry=").append(taxResidentCountry);
        sb.append(", foreignResidence=").append(foreignResidence);
        sb.append(", foreignResidenceCountry=").append(foreignResidenceCountry);
        sb.append(", usaResidence=").append(usaResidence);
        sb.append(", foreignTaxNumber='").append(foreignTaxNumber).append('\'');
        sb.append(", secretWord='").append(secretWord).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
