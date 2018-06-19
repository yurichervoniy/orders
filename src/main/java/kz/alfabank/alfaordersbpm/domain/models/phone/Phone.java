package kz.alfabank.alfaordersbpm.domain.models.phone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.Constants;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "order_phone"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id", "phone", "phone_type"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class Phone extends AuditInfo {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
    @SequenceGenerator(name = "phone_gen", sequenceName = "phone_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для телефона обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Тип телефона как ENUM", required = true, allowableValues = "MOBILE, HOME, WORK")
    @NotNull(message = "Тип адреса не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type", nullable = false, length = 50)
    private PhoneType phoneType;

    @ApiModelProperty(notes="Ссылка на тип телефона oCRM", required = true)
    @Embedded
    @NotNull(message = "Тип телефона oCRM не может быть пустым")
    private CommonServiceRef phoneTypeRef;

    @ApiModelProperty(notes="Телефон", required = true)
    @NotBlank(message = "Телефон не может быть пустым")
    @Size(min = Constants.PHONE_LENGTH, max = Constants.PHONE_LENGTH)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Номер телефона не соответсвует regexp-у")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

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

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public CommonServiceRef getPhoneTypeRef() {
        return phoneTypeRef;
    }

    public void setPhoneTypeRef(CommonServiceRef phoneTypeRef) {
        this.phoneTypeRef = phoneTypeRef;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Phone{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", order=").append(order);
        sb.append(", phoneType=").append(phoneType);
        sb.append(", phoneTypeRef=").append(phoneTypeRef);
        sb.append(", phone='").append(phone).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
