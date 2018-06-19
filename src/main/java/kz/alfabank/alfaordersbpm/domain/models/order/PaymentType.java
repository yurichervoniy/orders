package kz.alfabank.alfaordersbpm.domain.models.order;

import java.util.Objects;

public enum PaymentType {
    ANNUITY("аннуитет"),
    DIFFERENTIAL("дифференцированный");

    private final String paymentTypeName;

    PaymentType(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public static PaymentType parseFromCode(String code){
        if (code == null)
            throw new IllegalArgumentException("Code is null when trying to parse PaymentType");
        PaymentType paymentType;
        switch (code) {
            case "1" : paymentType = PaymentType.ANNUITY;
                break;
            case "2" : paymentType = PaymentType.DIFFERENTIAL;
                break;
            default : paymentType = null;
                break;
        }
        Objects.requireNonNull(paymentType, String.format("Could not parse PaymentType from input code %s",code));
        return paymentType;
    }


}
