package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.Constants;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class WorkDetailsDTO {

    @ApiModelProperty(notes="Наименование организации", required = true)
    @NotNull(message="Наименование организации не может быть пустым")
    private String organizationName;

    @ApiModelProperty(notes="Официальная занятость", required = true)
    @NotNull(message = "Официальная занятость не может быть пустой")
    private CommonServiceRef officialEmpRef;

    @ApiModelProperty(notes="Мобильный телефон", required = true)
    @NotBlank(message = "Мобильный телефон не может быть пустым")
    @Size(min = Constants.PHONE_LENGTH, max = Constants.PHONE_LENGTH)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Номер телефона не соответсвует regexp-у")
    private String phone;

    @ApiModelProperty(notes="Название должности", required = true)
    @NotNull(message = "Должность не может быть пустой")
    private CommonServiceRef positionNameRef;

    @ApiModelProperty(notes = "Отрасль деятельности", required = true)
    @NotNull(message = "Отрасль деятельности не может быть пустой")
    private CommonServiceRef industryTypeRef;

    @ApiModelProperty(notes="Заработная плата", required = true)
    @NotNull(message = "Заработная плата не может быть пустой")
    @Min(value = 0, message = "Заработная плата дожна быть больше либо равна нулю")
    @Positive(message = "Заработная плата дожна быть больше нуля")
    private BigDecimal salarySum;

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

    @ApiModelProperty(notes = "Продолжительность работы в месяцах",required = true)
    @NotNull(message = "Продолжительность работы не может быть пустой")
    private Integer workDuration;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public CommonServiceRef getOfficialEmpRef() {
        return officialEmpRef;
    }

    public void setOfficialEmpRef(CommonServiceRef officialEmpRef) {
        this.officialEmpRef = officialEmpRef;
    }

    public CommonServiceRef getLocalityTypeRef() {
        return localityTypeRef;
    }

    public void setLocalityTypeRef(CommonServiceRef localityTypeRef) {
        this.localityTypeRef = localityTypeRef;
    }

    public Integer getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Integer workDuration) {
        this.workDuration = workDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDetailsDTO that = (WorkDetailsDTO) o;
        return Objects.equals(organizationName, that.organizationName) &&
                Objects.equals(positionNameRef, that.positionNameRef) &&
                Objects.equals(industryTypeRef, that.industryTypeRef) &&
                Objects.equals(salarySum, that.salarySum) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(regionTypeRef, that.regionTypeRef) &&
                Objects.equals(districtTypeRef, that.districtTypeRef) &&
                Objects.equals(townTypeRef, that.townTypeRef) &&
                Objects.equals(localityTypeRef, that.localityTypeRef) &&
                Objects.equals(street, that.street) &&
                Objects.equals(microDistrict, that.microDistrict) &&
                Objects.equals(house, that.house) &&
                Objects.equals(flat, that.flat) &&
                Objects.equals(officialEmpRef, that.officialEmpRef) &&
                Objects.equals(workDuration, that.workDuration);
    }

    @Override
    public int hashCode() {

        return Objects.hash(organizationName, positionNameRef, industryTypeRef, salarySum, phone, regionTypeRef, districtTypeRef, townTypeRef, street, microDistrict, house, flat, officialEmpRef, localityTypeRef, workDuration);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WorkDetailsDTO{");
        sb.append("organizationName='").append(organizationName).append('\'');
        sb.append(", officialEmpRef=").append(officialEmpRef);
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", positionNameRef=").append(positionNameRef);
        sb.append(", industryTypeRef=").append(industryTypeRef);
        sb.append(", salarySum=").append(salarySum);
        sb.append(", regionTypeRef=").append(regionTypeRef);
        sb.append(", districtTypeRef=").append(districtTypeRef);
        sb.append(", townTypeRef=").append(townTypeRef);
        sb.append(", localityTypeRef=").append(localityTypeRef);
        sb.append(", street='").append(street).append('\'');
        sb.append(", microDistrict='").append(microDistrict).append('\'');
        sb.append(", house='").append(house).append('\'');
        sb.append(", flat='").append(flat).append('\'');
        sb.append(", workDuration=").append(workDuration);
        sb.append('}');
        return sb.toString();
    }
}
