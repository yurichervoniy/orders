package kz.alfabank.alfaordersbpm.domain.models.projections.order;

import kz.alfabank.alfaordersbpm.domain.models.order.OrderState;

import java.time.LocalDate;

public interface ProcAppInfo {

    Long getId();

    String getOrderNumber();

    LocalDate getOrderDate();

    String getStepUI();

    String getIin();

    OrderState getOrderState();

    String getPiid();
}
