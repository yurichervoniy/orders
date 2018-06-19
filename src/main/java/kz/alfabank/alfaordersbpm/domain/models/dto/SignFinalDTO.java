package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.OrderBinding;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SignFinalDTO implements OrderBinding {
    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для документов обязателен ID заявки")
    @Positive
    private Long orderId;

    @ApiModelProperty(notes="День платежа", required = true)
    @NotNull(message = "День платежа обязателен")
    @Positive
    private Long payDay;

    @ApiModelProperty(notes="Счет клиента", required = true)
    @NotNull(message = "Счет клиента обязателен")
    private String account;

    @ApiModelProperty(notes="Флаг карточного счета")
    private Long cardFl;

    @ApiModelProperty(notes="IDN карты")
    private String cardIDN;

    @ApiModelProperty(notes="Статус создания договора")
    private String status;

    @ApiModelProperty(notes="Флаг подписания документов")
    private Long signed;

    @ApiModelProperty(notes="Сегмент клиента")
    private String clientSegment;

    @Override
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
        return "SignFinalDTO{" +
                "orderId=" + orderId +
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
