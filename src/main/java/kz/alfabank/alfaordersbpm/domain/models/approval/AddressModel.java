package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(description = "Модель адреса")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressModel {
    @ApiModelProperty(notes="Район") private DictionaryModel districtTypeRef;
    @ApiModelProperty(notes="Микрорайон") private String microDistrict;
    @ApiModelProperty(notes="Улица") private String street;
    @ApiModelProperty(notes="Дом") private String house;
    @ApiModelProperty(notes="Квартира") private String flat;
    @ApiModelProperty(notes="Регион") private DictionaryModel regionTypeRef;
    @ApiModelProperty(notes="Тип населенного пункта") private DictionaryModel localityTypeRef;
    @ApiModelProperty(notes="Тип населенного пункта") private DictionaryModel townTypeRef;

    @JsonCreator
    public AddressModel(@JsonProperty("districtTypeRef") DictionaryModel districtTypeRef,
                        @JsonProperty("microDistrict") String microDistrict,
                        @JsonProperty("street") String street,
                        @JsonProperty("house") String house,
                        @JsonProperty("flat") String flat,
                        @JsonProperty("regionTypeRef") DictionaryModel regionTypeRef,
                        @JsonProperty("localityTypeRef") DictionaryModel localityTypeRef,
                        @JsonProperty("townTypeRef") DictionaryModel townTypeRef) {
        this.districtTypeRef = districtTypeRef;
        this.microDistrict = microDistrict;
        this.street = street;
        this.house = house;
        this.flat = flat;
        this.regionTypeRef = regionTypeRef;
        this.localityTypeRef = localityTypeRef;
        this.townTypeRef = townTypeRef;
    }

    public DictionaryModel getDistrictTypeRef() {
        return districtTypeRef;
    }

    public void setDistrictTypeRef(DictionaryModel districtTypeRef) {
        this.districtTypeRef = districtTypeRef;
    }

    public String getMicroDistrict() {
        return microDistrict;
    }

    public void setMicroDistrict(String microDistrict) {
        this.microDistrict = microDistrict;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public DictionaryModel getRegionTypeRef() {
        return regionTypeRef;
    }

    public void setRegionTypeRef(DictionaryModel regionTypeRef) {
        this.regionTypeRef = regionTypeRef;
    }

    public DictionaryModel getLocalityTypeRef() {
        return localityTypeRef;
    }

    public void setLocalityTypeRef(DictionaryModel localityTypeRef) {
        this.localityTypeRef = localityTypeRef;
    }

    public DictionaryModel getTownTypeRef() {
        return townTypeRef;
    }

    public void setTownTypeRef(DictionaryModel townTypeRef) {
        this.townTypeRef = townTypeRef;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "districtTypeRef=" + districtTypeRef +
                ", microDistrict='" + microDistrict + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", flat='" + flat + '\'' +
                ", regionTypeRef=" + regionTypeRef +
                ", localityTypeRef=" + localityTypeRef +
                ", townTypeRef=" + townTypeRef +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressModel that = (AddressModel) o;
        return Objects.equals(districtTypeRef, that.districtTypeRef) &&
                Objects.equals(microDistrict, that.microDistrict) &&
                Objects.equals(street, that.street) &&
                Objects.equals(house, that.house) &&
                Objects.equals(flat, that.flat) &&
                Objects.equals(regionTypeRef, that.regionTypeRef) &&
                Objects.equals(localityTypeRef, that.localityTypeRef) &&
                Objects.equals(townTypeRef, that.townTypeRef);
    }

    @Override
    public int hashCode() {

        return Objects.hash(districtTypeRef, microDistrict, street, house, flat, regionTypeRef, localityTypeRef, townTypeRef);
    }
}
