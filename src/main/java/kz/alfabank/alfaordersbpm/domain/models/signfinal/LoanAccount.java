package kz.alfabank.alfaordersbpm.domain.models.signfinal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_ACCOUNT")
@RowId("ROWID")
public class LoanAccount {
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

    @ApiModelProperty(notes="Счет клиента")
    @Column(name = "acc_iban", length = 1000, updatable = false)
    private String account;

    @ApiModelProperty(notes="Признак карточного счета")
    @Column(name = "card_fl", length = 1000, updatable = false)
    private String cardFl;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCardFl() {
        return cardFl;
    }

    public void setCardFl(String cardFl) {
        this.cardFl = cardFl;
    }

    @Override
    public String toString() {
        return "LoanAccount{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", order=" + order +
                ", account='" + account + '\'' +
                ", cardFl='" + cardFl + '\'' +
                '}';
    }
}
