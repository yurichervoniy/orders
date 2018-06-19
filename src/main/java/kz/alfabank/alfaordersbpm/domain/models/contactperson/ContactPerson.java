package kz.alfabank.alfaordersbpm.domain.models.contactperson;

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
@Table(name = "ORDER_CONTACT_PERSON"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id", "phone"})}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class ContactPerson extends AuditInfo {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contactperson_gen")
    @SequenceGenerator(name = "contactperson_gen", sequenceName = "contact_person_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для телефона обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Телефон", required = true)
    @NotBlank(message = "Телефон не может быть пустым")
    @Size(min = Constants.PHONE_LENGTH, max = Constants.PHONE_LENGTH)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Номер телефона не соответсвует regexp-у")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @ApiModelProperty(notes="Наименование субъекта", required = true)
    @NotBlank(message = "Наименование субъекта не может быть пустым")
    @Column(name = "person_name", nullable = false, length = 1000)
    private String personName;

    @ApiModelProperty(notes="Ссылка на степень родства oCRM", required = true)
    @Embedded
    @NotNull(message = "Ссылка на степень родства oCRM не может быть пустой")
    private CommonServiceRef relationTypeRef;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public CommonServiceRef getRelationTypeRef() {
        return relationTypeRef;
    }

    public void setRelationTypeRef(CommonServiceRef relationTypeRef) {
        this.relationTypeRef = relationTypeRef;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContactPerson{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", personName='").append(personName).append('\'');
        sb.append(", relationTypeRef=").append(relationTypeRef);
        sb.append('}');
        return sb.toString();
    }
}
