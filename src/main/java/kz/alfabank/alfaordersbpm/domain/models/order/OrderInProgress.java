package kz.alfabank.alfaordersbpm.domain.models.order;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders_inprogress")
@RowId("ROWID")
@EntityListeners({AuditingEntityListener.class})
public class OrderInProgress extends AuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderprogress_gen")
    @SequenceGenerator(name = "orderprogress_gen", sequenceName = "orderprogress_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для OrderInProgress обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false, unique = true)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="ИИН субъекта", required = true)
    @NotBlank(message = "ИИН OrderInProgress не может быть пустым")
    @Column(name="iin", length = 12, nullable = false, unique = true)
    private String iin;

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

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    @Override
    public String toString() {
        return "OrderInProgress{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", iin='" + iin + '\'' +
                '}';
    }

    public static OrderInProgress of(@NotNull(message = "Для OrderInProgress обязательно ID заявки") Long orderId,
                                     @NotBlank(message = "ИИН OrderInProgress не может быть пустым") String iin){
        OrderInProgress p = new OrderInProgress();
        p.setOrderId(orderId);
        p.setIin(iin);
        return p;
    }
}
