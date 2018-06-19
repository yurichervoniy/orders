package kz.alfabank.alfaordersbpm.domain.models.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_address"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id", "address_type"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class Address extends AuditInfo {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для адреса обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Тип адреса как ENUM", required = true, allowableValues = "REGISTRATION, WORK, RESIDENCE")
    @NotNull(message = "Тип адреса не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false, length = 50)
    private AddressType addressType;

    @ApiModelProperty(notes="Ссылка на тип адреса oCRM", required = true)
    @Embedded
    @NotNull(message = "Тип адреса oCRM не может быть пустым")
    private CommonServiceRef addressTypeRef;

    @ApiModelProperty(notes="Область, гор. респ. значения РК", required = true)
    @NotNull(message = "Регион КАТО не может быть пустым")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "rgn_code",nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "rgn_name",nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "rgn_source",nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "rgn_name_lang",nullable = false))
    })
    private CommonServiceRef regionTypeRef;

    @ApiModelProperty(notes="Районы РК")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "dstr_code")),
            @AttributeOverride(name = "text", column = @Column(name = "dstr_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "dstr_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "dstr_name_lang"))
    })
    private CommonServiceRef districtTypeRef;

    @ApiModelProperty(notes="Населенные пункты РК")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "twn_code")),
            @AttributeOverride(name = "text", column = @Column(name = "twn_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "twn_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "twn_name_lang"))
    })
    private CommonServiceRef townTypeRef;

    @ApiModelProperty(notes="Тип населенного пункта РК")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "twn_type_code")),
            @AttributeOverride(name = "text", column = @Column(name = "twn_type_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "twn_type_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "twn_type_name_lang"))
    })
    private CommonServiceRef localityTypeRef;

    @ApiModelProperty(notes="Улица")
    @Column(name = "street")
    private String street;

    @ApiModelProperty(notes="Микрорайон")
    @Column(name = "micro_district")
    private String microDistrict;

    @ApiModelProperty(notes="Дом", required = true)
    @NotNull(message = "Дом не может быть пустым")
    @Column(name = "house", nullable = false, length = 50)
    private String house;

    @ApiModelProperty(notes="Квартира")
    @Column(name = "flat", length = 50)
    private String flat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public AbstractOrder getOrder() {
        return order;
    }

    public void setOrder(AbstractOrder order) {
        this.order = order;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public CommonServiceRef getAddressTypeRef() {
        return addressTypeRef;
    }

    public void setAddressTypeRef(CommonServiceRef addressTypeRef) {
        this.addressTypeRef = addressTypeRef;
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

    public CommonServiceRef getLocalityTypeRef() {
        return localityTypeRef;
    }

    public void setLocalityTypeRef(CommonServiceRef localityTypeRef) {
        this.localityTypeRef = localityTypeRef;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", addressType=").append(addressType);
        sb.append(", addressTypeRef=").append(addressTypeRef);
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
