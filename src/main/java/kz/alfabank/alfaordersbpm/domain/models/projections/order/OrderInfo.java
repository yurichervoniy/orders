package kz.alfabank.alfaordersbpm.domain.models.projections.order;

import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.PaymentType;

import java.time.LocalDate;

public interface OrderInfo {

    String getOrderNumber();

    LocalDate getOrderDate();

    CommonServiceRef getCreditProductRef();

    CommonServiceRef getPaymentTypeRef();

    PaymentType getPaymentType();

    String getOrgCode();

    String getOrgName();

    CommonServiceRef getInsuranceRef();

}
