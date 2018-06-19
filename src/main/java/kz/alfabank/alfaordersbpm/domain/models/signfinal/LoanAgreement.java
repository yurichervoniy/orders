package kz.alfabank.alfaordersbpm.domain.models.signfinal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_LOAN")
@RowId("ROWID")
public class LoanAgreement {
    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Статус")
    @Column(name = "state", length = 1000, updatable = false)
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoanAgreement{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", order=" + order +
                ", status='" + status + '\'' +
                '}';
    }
}
