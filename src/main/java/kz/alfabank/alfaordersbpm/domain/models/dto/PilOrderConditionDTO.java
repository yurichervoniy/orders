package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Objects;

public class PilOrderConditionDTO {

    @ApiModelProperty(notes="Кредитный продукт", required = true)
    @NotNull(message = "Кредитный продукт не может быть пустым")
    private CommonServiceRef creditProductRef;

    @ApiModelProperty(notes="Запрошенная сумма", required = true)
    @NotNull(message = "Запрошенная сумма не может быть пустой")
    @Min(value = 0, message = "Запрошенная сумма дожна быть больше либо равна нулю")
    @Positive(message = "Запрошенная сумма дожна быть больше нуля")
    private BigDecimal requestedAmount;

    @ApiModelProperty(notes="Вид платежа", required = true)
    @NotNull(message = "Вид платежа не может быть пустым")
    private CommonServiceRef paymentTypeRef;

    @ApiModelProperty(notes="Страховка", required = true)
    @NotNull(message = "Ссылка на страховку не может быть пустой")
    private CommonServiceRef insuranceRef;

    @ApiModelProperty(notes="Сумма страховки", required = true)
    @NotNull(message = "Сумма страховки не может быть пустой")
    @Min(value = 0, message = "Сумма страховки дожна быть больше либо равна нулю")
    private BigDecimal insuranceAmount;

    @ApiModelProperty(notes="Регулярный платеж", required = true)
    @NotNull(message = "Регулярный платеж не может быть пустым")
    @Min(value = 0, message = "Регулярный платеж дожен быть больше либо равен нулю")
    @Positive(message = "Регулярный платеж дожен быть больше нуля")
    private BigDecimal regularPayment;

    @ApiModelProperty(notes="Запрашиваемый срок кредита", required = true)
    @NotNull(message = "Запрашиваемый срок кредита не может быть пустым")
    @Min(value = 1, message = "Запрашиваемый срок должен быть больше либо равен еденице")
    @Positive(message = "Запрашиваемый срок должен быть больше нуля")
    private Integer loanDuration;

    @ApiModelProperty(notes="Дополнительный доход", required = true)
    @NotNull(message = "Дополнительный доход не может быть пустым")
    @Min(value = 0, message = "Дополнительный доход дожен быть больше либо равен нулю")
    @PositiveOrZero(message = "ЗДополнительный доход должен быть больше либо равен нулю")
    private BigDecimal additionalIncome;

    @ApiModelProperty(notes="Сумма переплаты")
    private BigDecimal overPayment;

    @ApiModelProperty(notes="Привлеченец")
    private CommonServiceRef attractedRef;

    public CommonServiceRef getCreditProductRef() {
        return creditProductRef;
    }

    public void setCreditProductRef(CommonServiceRef creditProductRef) {
        this.creditProductRef = creditProductRef;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public CommonServiceRef getPaymentTypeRef() {
        return paymentTypeRef;
    }

    public void setPaymentTypeRef(CommonServiceRef paymentTypeRef) {
        this.paymentTypeRef = paymentTypeRef;
    }

    public CommonServiceRef getInsuranceRef() {
        return insuranceRef;
    }

    public void setInsuranceRef(CommonServiceRef insuranceRef) {
        this.insuranceRef = insuranceRef;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public BigDecimal getRegularPayment() {
        return regularPayment;
    }

    public void setRegularPayment(BigDecimal regularPayment) {
        this.regularPayment = regularPayment;
    }

    public Integer getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(Integer loanDuration) {
        this.loanDuration = loanDuration;
    }

    public BigDecimal getAdditionalIncome() {
        return additionalIncome;
    }

    public void setAdditionalIncome(BigDecimal additionalIncome) {
        this.additionalIncome = additionalIncome;
    }

    public CommonServiceRef getAttractedRef() {
        return attractedRef;
    }

    public void setAttractedRef(CommonServiceRef attractedRef) {
        this.attractedRef = attractedRef;
    }

    public BigDecimal getOverPayment() {
        return overPayment;
    }

    public void setOverPayment(BigDecimal overPayment) {
        this.overPayment = overPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PilOrderConditionDTO that = (PilOrderConditionDTO) o;
        return Objects.equals(creditProductRef, that.creditProductRef) &&
                Objects.equals(requestedAmount, that.requestedAmount) &&
                Objects.equals(paymentTypeRef, that.paymentTypeRef) &&
                Objects.equals(insuranceRef, that.insuranceRef) &&
                Objects.equals(insuranceAmount, that.insuranceAmount) &&
                Objects.equals(regularPayment, that.regularPayment) &&
                Objects.equals(loanDuration, that.loanDuration) &&
                Objects.equals(additionalIncome, that.additionalIncome) &&
                Objects.equals(attractedRef, that.attractedRef) &&
                Objects.equals(overPayment, that.overPayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditProductRef, requestedAmount, paymentTypeRef, insuranceRef, insuranceAmount, regularPayment, loanDuration, additionalIncome, attractedRef, overPayment);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PilOrderConditionDTO{");
        sb.append("creditProductRef=").append(creditProductRef);
        sb.append(", requestedAmount=").append(requestedAmount);
        sb.append(", paymentTypeRef=").append(paymentTypeRef);
        sb.append(", insuranceRef=").append(insuranceRef);
        sb.append(", insuranceAmount=").append(insuranceAmount);
        sb.append(", regularPayment=").append(regularPayment);
        sb.append(", loanDuration=").append(loanDuration);
        sb.append(", additionalIncome=").append(additionalIncome);
        sb.append(", overPayment=").append(overPayment);
        sb.append(", attractedRef=").append(attractedRef);
        sb.append('}');
        return sb.toString();
    }
}
