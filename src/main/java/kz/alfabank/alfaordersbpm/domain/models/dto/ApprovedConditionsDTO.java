package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ApprovedConditionsDTO {

    @ApiModelProperty(notes="Ставка ГЭСВ")
    @NotNull(message = "Ставка ГЭСВ не может быть пустой")
    @Min(value = 0, message = "Ставка ГЭСВ дожна быть больше либо равна нулю")
    private BigDecimal gesv;

    @ApiModelProperty(notes="Переплата")
    @NotNull(message = "Переплата не может быть пустой")
    @Min(value = 0, message = "Переплата дожна быть больше либо равна нулю")
    private BigDecimal overPayment;

    @ApiModelProperty(notes="Cрок кредита", required = true)
    @NotNull(message = "Cрок кредита не может быть пустым")
    @Min(value = 1, message = "Одобренынй срок должен быть больше либо равен еденице")
    private BigDecimal approvedDuration;

    public BigDecimal getGesv() {
        return gesv;
    }

    public void setGesv(BigDecimal gesv) {
        this.gesv = gesv;
    }

    public BigDecimal getOverPayment() {
        return overPayment;
    }

    public void setOverPayment(BigDecimal overPayment) {
        this.overPayment = overPayment;
    }

    public BigDecimal getApprovedDuration() {
        return approvedDuration;
    }

    public void setApprovedDuration(BigDecimal approvedDuration) {
        this.approvedDuration = approvedDuration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApprovedConditionsDTO{");
        sb.append("gesv=").append(gesv);
        sb.append(", overPayment=").append(overPayment);
        sb.append('}');
        return sb.toString();
    }

}
