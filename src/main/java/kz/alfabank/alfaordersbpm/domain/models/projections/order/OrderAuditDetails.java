package kz.alfabank.alfaordersbpm.domain.models.projections.order;

public interface OrderAuditDetails {

    String getId();

    String getAuditid();

    String getPropertyname();

    String getOldvalue();

    String getNewValue();
}
