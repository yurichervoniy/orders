package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.OrderBinding;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressType;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class AddressDTO implements OrderBinding {

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для адреса обязательно ID заявки")
    @Positive
    private Long orderId;

    @ApiModelProperty(notes="Тип адреса", required = true)
    @NotNull(message = "Тип адреса не может быть пустым")
    private AddressType addressType;

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

    @Override
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public CommonServiceRef getLocalityTypeRef() {
        return localityTypeRef;
    }

    public void setLocalityTypeRef(CommonServiceRef localityTypeRef) {
        this.localityTypeRef = localityTypeRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDTO that = (AddressDTO) o;
        return Objects.equals(orderId, that.orderId) &&
                addressType == that.addressType &&
                Objects.equals(regionTypeRef, that.regionTypeRef) &&
                Objects.equals(districtTypeRef, that.districtTypeRef) &&
                Objects.equals(townTypeRef, that.townTypeRef) &&
                Objects.equals(localityTypeRef, that.localityTypeRef) &&
                Objects.equals(street, that.street) &&
                Objects.equals(microDistrict, that.microDistrict) &&
                Objects.equals(house, that.house) &&
                Objects.equals(flat, that.flat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orderId, addressType, regionTypeRef, districtTypeRef, townTypeRef, street, microDistrict, house, flat, localityTypeRef);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddressDTO{");
        sb.append("orderId=").append(orderId);
        sb.append(", addressType=").append(addressType);
        sb.append(", regionTypeRef=").append(regionTypeRef);
        sb.append(", districtTypeRef=").append(districtTypeRef);
        sb.append(", townTypeRef=").append(townTypeRef);
        sb.append(", localityTypeRef=").append(localityTypeRef);
        sb.append(", street='").append(street).append('\'');
        sb.append(", microDistrict='").append(microDistrict).append('\'');
        sb.append(", house='").append(house).append('\'');
        sb.append(", flat='").append(flat).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
