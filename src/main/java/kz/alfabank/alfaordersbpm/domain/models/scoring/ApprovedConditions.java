package kz.alfabank.alfaordersbpm.domain.models.scoring;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.RowId;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_ApprovedConditions"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id"})
}
)
@RowId("ROWID")
public class ApprovedConditions {

    private static final BigDecimal PERCENT = BigDecimal.valueOf(100);

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approved_gen")
    @SequenceGenerator(name = "approved_gen", sequenceName = "approved_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для телефона обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @ApiModelProperty(notes="Одобренная сумма", required = true)
    @NotNull(message = "Одобренная сумма не может быть пустой")
    @Column(name = "approved_amount", nullable = false,  precision = 19, scale = 4)
    private BigDecimal approvedAmount;

    @ApiModelProperty(notes="Запрошенная сумма", required = true)
    @NotNull(message = "Запрошенная сумма не может быть пустой")
    @Min(value = 0, message = "Запрошенная сумма дожна быть больше либо равна нулю")
    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @ApiModelProperty(notes="Код продукта")
    @Column(name = "product_сode", length = 1000)
    private String productCode;

    @ApiModelProperty(notes="Валюта займа", required = true)
    @NotBlank(message = "Валюта займа не может быть пустой")
    @Column(name = "currency", nullable = false, length = 1000)
    private String currency;

    @ApiModelProperty(notes="Одобренынй срок кредита", required = true)
    @NotNull(message = "Одобренынй срок кредита не может быть пустым")
    @Min(value = 1, message = "Одобренынй срок должен быть больше либо равен еденице")
    @Column(name = "approved_duration",nullable = false, precision = 10, scale = 2)
    private BigDecimal approvedDuration;

    @ApiModelProperty(notes="Запрашиваемый срок кредита", required = true)
    @NotNull(message = "Запрашиваемый срок кредита не может быть пустым")
    @Min(value = 1, message = "Запрашиваемый срок должен быть больше либо равен еденице")
    @Column(name = "requested_duration",nullable = false)
    private Integer requestedDuration;

    @ApiModelProperty(notes="Одобренная ставка", required = true)
    @NotNull(message = "Одобренная ставка не может быть пустой")
    @Min(value = 0, message = "Одобренная ставка дожна быть больше либо равна нулю")
    @Column(name = "approved_rate", nullable = false, precision = 19, scale = 4)
    private BigDecimal approvedRate;

    @ApiModelProperty(notes="Ставка ГЭСВ")
    @Column(name = "approved_gesv", precision = 19, scale = 4)
    private BigDecimal gesv;

    @ApiModelProperty(notes="Сегмент клиента")
    @Column(name = "client_segment", length = 1000)
    private String clientSegment;

    @ApiModelProperty(notes="Метод погашения")
    @Column(name = "payment_type", length = 1000)
    private String paymentType;

    @ApiModelProperty(notes="Комиссия за предоставление займа")
    @Column(name = "loan_commission", precision = 19, scale = 4)
    private BigDecimal loanCommission;

    @ApiModelProperty(notes="Цель кредитования")
    @Column(name = "credit_purpose", length = 1000)
    private String creditPurpose;

    @ApiModelProperty(notes="Вид бизнеса")
    @Column(name = "business", length = 1000)
    private String business;

    @ApiModelProperty(notes="Комиссия за ведения текущего счета")
    @Column(name = "account_maintenance_fee", precision = 19, scale = 4)
    private BigDecimal accountMaintenanceFee;

    @ApiModelProperty(notes="Аналитика PIL")
    @Column(name = "pil_analytic", length = 1000)
    private String pilAnalytic;

    @ApiModelProperty(notes="Ежемесячный платеж")
    @Column(name = "monthly_payment", precision = 19, scale = 4)
    private BigDecimal monthlyPayment;

    @ApiModelProperty(notes="Переплата")
    @Column(name = "overPayment", precision = 19, scale = 4)
    private BigDecimal overPayment;

    @ApiModelProperty(notes="Сумма страховки")
    @Column(name = "insurance_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal insuranceAmount;

    @ApiModelProperty(notes="Решение скоринга")
    @Column(name = "decision_text", length = 1000)
    private String decisionText;

    @ApiModelProperty(notes="Категория решение скоринга")
    @Column(name = "decision_category", length = 1000)
    private String decisionCategory;

    @ApiModelProperty(notes="Код типа платежа")
    @Column(name = "payment_type_code", length = 1000)
    private String paymentTypeCode;


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

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getApprovedDuration() {
        return approvedDuration;
    }

    public void setApprovedDuration(BigDecimal approvedDuration) {
        this.approvedDuration = approvedDuration;
    }

    public Integer getRequestedDuration() {
        return requestedDuration;
    }

    public void setRequestedDuration(Integer requestedDuration) {
        this.requestedDuration = requestedDuration;
    }

    public BigDecimal getApprovedRate() {
        return approvedRate;
    }

    public void setApprovedRate(BigDecimal approvedRate) {
        this.approvedRate = approvedRate;
    }

    public BigDecimal getGesv() {
        return gesv;
    }

    public void setGesv(BigDecimal gesv) {
        this.gesv = gesv;
    }

    public String getClientSegment() {
        return clientSegment;
    }

    public void setClientSegment(String clientSegment) {
        this.clientSegment = clientSegment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getLoanCommission() {
        return loanCommission;
    }

    public void setLoanCommission(BigDecimal loanCommission) {
        this.loanCommission = loanCommission;
    }

    public String getCreditPurpose() {
        return creditPurpose;
    }

    public void setCreditPurpose(String creditPurpose) {
        this.creditPurpose = creditPurpose;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public BigDecimal getAccountMaintenanceFee() {
        return accountMaintenanceFee;
    }

    public void setAccountMaintenanceFee(BigDecimal accountMaintenanceFee) {
        this.accountMaintenanceFee = accountMaintenanceFee;
    }

    public String getPilAnalytic() {
        return pilAnalytic;
    }

    public void setPilAnalytic(String pilAnalytic) {
        this.pilAnalytic = pilAnalytic;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getOverPayment() {
        return overPayment;
    }

    public void setOverPayment(BigDecimal overPayment) {
        this.overPayment = overPayment;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getDecisionCategory() {
        return decisionCategory;
    }

    public void setDecisionCategory(String decisionCategory) {
        this.decisionCategory = decisionCategory;
    }

    public BigDecimal getApprovedRatePercent() {
        return approvedRate == null ? null : approvedRate.multiply(PERCENT);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApprovedConditions{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", approvedAmount=").append(approvedAmount);
        sb.append(", requestedAmount=").append(requestedAmount);
        sb.append(", productCode='").append(productCode).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", approvedDuration=").append(approvedDuration);
        sb.append(", requestedDuration=").append(requestedDuration);
        sb.append(", approvedRate=").append(approvedRate);
        sb.append(", gesv=").append(gesv);
        sb.append(", clientSegment='").append(clientSegment).append('\'');
        sb.append(", paymentType='").append(paymentType).append('\'');
        sb.append(", loanCommission=").append(loanCommission);
        sb.append(", creditPurpose='").append(creditPurpose).append('\'');
        sb.append(", business='").append(business).append('\'');
        sb.append(", accountMaintenanceFee=").append(accountMaintenanceFee);
        sb.append(", pilAnalytic='").append(pilAnalytic).append('\'');
        sb.append(", monthlyPayment=").append(monthlyPayment);
        sb.append(", overPayment=").append(overPayment);
        sb.append(", insuranceAmount=").append(insuranceAmount);
        sb.append(", decisionText='").append(decisionText).append('\'');
        sb.append(", decisionCategory='").append(decisionCategory).append('\'');
        sb.append(", paymentTypeCode='").append(paymentTypeCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
