package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.Constants;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;


public class PersonDetailsDTO {

    @ApiModelProperty(notes="ИИН субъекта", required = true)
    @NotBlank(message = "ИИН не может быть пустым")
    @Size(min = 12, max = 12, message = "Длина ИИН должна быть 12 символов")
    @Pattern(regexp = "[0-9]{12}", message = "В ИИН-не допустимы только цифры")
    private String iin;

    @ApiModelProperty(notes="Фамилия субъекта", required = true)
    @Size(max = 100, message = "Длина Фамилии не должна первышать 100 символов") // Для оралманов 0
    private String lastName;

    @ApiModelProperty(notes="Имя субъекта")
    @Size(max = 100, message = "Длина имени не должна первышать 100 символов")
    private String firstName;

    @ApiModelProperty(notes="Отчество субъекта")
    @Size(max = 100, message = "Длина отчества не должна первышать 100 символов")
    private String middleName;

    @ApiModelProperty(notes="Дата рождения субъекта", required = true)
    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть меньше текущего дня")
    private LocalDate birthDate;

    @ApiModelProperty(notes="Пол субъекта", required = true)
    @NotNull(message = "Пол субъекта не может быть пустым")
    private CommonServiceRef genderRef;

    @ApiModelProperty(notes="Мобильный телефон", required = true)
    @NotBlank(message = "Мобильный телефон не может быть пустым")
    @Size(min = Constants.PHONE_LENGTH, max = Constants.PHONE_LENGTH)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Номер телефона не соответсвует regexp-у")
    private String mobilePhone;

    @ApiModelProperty(notes="Код подтверждения", required = true)
    @NotBlank(message = "Код подтверждения не может быть пустым")
    private String confirmationCode;

    @ApiModelProperty(notes="Регион КАТО", required = true)
    @NotNull(message = "Регион КАТО не может быть пустым")
    private CommonServiceRef regionTypeRef;

    @ApiModelProperty(notes="Район КАТО", required = true)
    //@NotNull(message = "Район КАТО не может быть пустым")
    private CommonServiceRef districtTypeRef;

    @ApiModelProperty(notes="Город КАТО")
    //@NotNull(message = "Город КАТО не может быть пустым")
    private CommonServiceRef townTypeRef;

    @ApiModelProperty(notes = "Тип населенного пункта")
    private CommonServiceRef localityTypeRef;

    @ApiModelProperty(notes="Улица")
    private String street;

    @ApiModelProperty(notes="Микрорайон")
    private String microDistrict;

    @ApiModelProperty(notes="Дом", required = true)
    @NotNull(message = "Дом не может быть пустым")
    private String house;

    @ApiModelProperty(notes="Квартира")
    private String flat;

    @ApiModelProperty(notes = "Специальная отметка", required = true)
    @NotNull(message = "Спеециальная отметка не может быть пустой")
    private Integer specialMark;

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

    public CommonServiceRef getGenderRef() {
        return genderRef;
    }

    public void setGenderRef(CommonServiceRef genderRef) {
        this.genderRef = genderRef;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public CommonServiceRef getRegionTypeRef() {
        return regionTypeRef;
    }

    public void setRegionTypeRef(CommonServiceRef regionTypeRef) {
        this.regionTypeRef = regionTypeRef;
    }

    public CommonServiceRef getDistrictTypeRef() {
        return districtTypeRef;
    }

    public void setDistrictTypeRef(CommonServiceRef districtTypeRef) {
        this.districtTypeRef = districtTypeRef;
    }

    public CommonServiceRef getTownTypeRef() {
        return townTypeRef;
    }

    public void setTownTypeRef(CommonServiceRef townTypeRef) {
        this.townTypeRef = townTypeRef;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMicroDistrict() {
        return microDistrict;
    }

    public void setMicroDistrict(String microDistrict) {
        this.microDistrict = microDistrict;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public CommonServiceRef getLocalityTypeRef() {
        return localityTypeRef;
    }

    public void setLocalityTypeRef(CommonServiceRef localityTypeRef) {
        this.localityTypeRef = localityTypeRef;
    }

    public Integer getSpecialMark() {
        return specialMark;
    }

    public void setSpecialMark(Integer specialMark) {
        this.specialMark = specialMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDetailsDTO that = (PersonDetailsDTO) o;
        return Objects.equals(iin, that.iin) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(birthDate, that.birthDate) &&
                Objects.equals(genderRef, that.genderRef) &&
                Objects.equals(mobilePhone, that.mobilePhone) &&
                Objects.equals(regionTypeRef, that.regionTypeRef) &&
                Objects.equals(districtTypeRef, that.districtTypeRef) &&
                Objects.equals(townTypeRef, that.townTypeRef) &&
                Objects.equals(localityTypeRef, that.localityTypeRef) &&
                Objects.equals(street, that.street) &&
                Objects.equals(microDistrict, that.microDistrict) &&
                Objects.equals(house, that.house) &&
                Objects.equals(flat, that.flat) &&
                Objects.equals(specialMark, that.specialMark);
    }

    @Override
    public int hashCode() {

        return Objects.hash(iin, lastName, firstName, middleName, birthDate, genderRef, mobilePhone, regionTypeRef, districtTypeRef, townTypeRef, street, microDistrict, house, flat, localityTypeRef,  specialMark);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersonDetailsDTO{");
        sb.append("iin='").append(iin).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", birthDate=").append(birthDate);
        sb.append(", genderRef=").append(genderRef);
        sb.append(", mobilePhone='").append(mobilePhone).append('\'');
        sb.append(", confirmationCode='").append(confirmationCode).append('\'');
        sb.append(", regionTypeRef=").append(regionTypeRef);
        sb.append(", districtTypeRef=").append(districtTypeRef);
        sb.append(", townTypeRef=").append(townTypeRef);
        sb.append(", localityTypeRef=").append(localityTypeRef);
        sb.append(", street='").append(street).append('\'');
        sb.append(", microDistrict='").append(microDistrict).append('\'');
        sb.append(", house='").append(house).append('\'');
        sb.append(", flat='").append(flat).append('\'');
        sb.append(", specialMark='").append(specialMark).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
