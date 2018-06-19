package kz.alfabank.alfaordersbpm.domain.models.projections.order;

import kz.alfabank.alfaordersbpm.domain.models.order.OrderState;

import java.time.LocalDate;

public interface MainInfo {

    Long getId();

    String getOrderNumber();

    LocalDate getOrderDate();

    String getStepUI();

    String getOrgName();

    OrderState getOrderState();

    String getPiid();

}
