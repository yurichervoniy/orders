package kz.alfabank.alfaordersbpm.domain.models.signfinal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.Constants;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "order_signfinal",uniqueConstraints={@UniqueConstraint(columnNames = {"order_id"})})
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class SignFinal extends AuditInfo {
    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "signfinal_gen")
    @SequenceGenerator(name = "signfinal_gen", sequenceName = "signfinal_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для подписания документов обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="День платежа", required = true)
    @NotNull(message = "День платежа не может быть пустым")
    @Max(Constants.PAYDAY_MAX)
    @Min(Constants.PAYDAY_MIN)
    @Column(name = "payday", nullable = false, length = 20)
    private Long payDay;

    @ApiModelProperty(notes="Счет", required = true)
    @NotBlank(message = "Счет не может быть пустым")
    @Column(name = "account", nullable = false, length = 20)
    private String account;

    @ApiModelProperty(notes="Признак карточного счета", required = true)
    @NotNull(message = "Признак карточного счета не может быть пустым")
    @Column(name = "card_fl", nullable = false, length = 20)
    private Long cardFl;

    @ApiModelProperty(notes="Карточный счет")
    @Column(name = "card_idn", length = 20)
    private String cardIDN;

    @ApiModelProperty(notes="Статус создания договора", required = true)
    @Column(name = "status", length = 20)
    private String status;

    @ApiModelProperty(notes="Договор подписан", required = true)
    @Column(name = "signed", length = 20)
    private Long signed;

    @ApiModelProperty(notes="Договор подписан", required = true)
    @Column(name = "client_segment", length = 20)
    private String clientSegment;

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

    public Long getPayDay() {
        return payDay;
    }

    public void setPayDay(Long payDay) {
        this.payDay = payDay;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getCardFl() {
        return cardFl;
    }

    public void setCardFl(Long cardFl) {
        this.cardFl = cardFl;
    }

    public String getCardIDN() {
        return cardIDN;
    }

    public void setCardIDN(String cardIDN) {
        this.cardIDN = cardIDN;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSigned() {
        return signed;
    }

    public void setSigned(Long signed) {
        this.signed = signed;
    }

    public String getClientSegment() {
        return clientSegment;
    }

    public void setClientSegment(String clientSegment) {
        this.clientSegment = clientSegment;
    }

    @Override
    public String toString() {
        return "SignFinal{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", order=" + order +
                ", payDay=" + payDay +
                ", account='" + account + '\'' +
                ", cardFl=" + cardFl +
                ", cardIDN='" + cardIDN + '\'' +
                ", status='" + status + '\'' +
                ", signed=" + signed +
                ", clientSegment='" + clientSegment + '\'' +
                '}';
    }
}
