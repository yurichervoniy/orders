package kz.alfabank.alfaordersbpm.domain.models.projections.order;

public interface OrderAuditOperations {

    String getid();

    String getStamp();

    String getDbuser();

    String getUsername();

    String getOrderid();

    String getEntityname();

    String getEntityid();

    String getOperation();
}
