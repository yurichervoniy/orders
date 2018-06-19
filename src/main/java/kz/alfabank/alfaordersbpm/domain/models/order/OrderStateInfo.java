package kz.alfabank.alfaordersbpm.domain.models.order;

public class OrderStateInfo {

    private final String orderStepUI;
    private final String processStepUI;

    public OrderStateInfo(String orderStepUI, String processStepUI) {
        this.orderStepUI = orderStepUI;
        this.processStepUI = processStepUI;
    }

    public String getOrderStepUI() {
        return orderStepUI;
    }

    public String getProcessStepUI() {
        return processStepUI;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderStateInfo{");
        sb.append("orderStepUI='").append(orderStepUI).append('\'');
        sb.append(", processStepUI='").append(processStepUI).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
