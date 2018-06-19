package kz.alfabank.alfaordersbpm.domain.models.dto;

import kz.alfabank.alfaordersbpm.domain.models.order.OrderState;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.ProcAppInfo;

import java.time.LocalDate;

public class ProcAppInfoDTO implements ProcAppInfo {

    private final ProcAppInfo procAppInfo;
    private final String processSep;

    public ProcAppInfoDTO(ProcAppInfo procAppInfo, String processSep){
        this.procAppInfo = procAppInfo;
        this.processSep = processSep;
    }

    @Override
    public Long getId() {
        return procAppInfo.getId();
    }

    @Override
    public String getOrderNumber() {
        return procAppInfo.getOrderNumber();
    }

    @Override
    public LocalDate getOrderDate() {
        return procAppInfo.getOrderDate();
    }

    @Override
    public String getStepUI() {
        return processSep;
    }

    @Override
    public String getIin() {
        return procAppInfo.getIin();
    }

    @Override
    public OrderState getOrderState() {
        return procAppInfo.getOrderState();
    }

    @Override
    public String getPiid() {
        return procAppInfo.getPiid();
    }
}
