package kz.alfabank.alfaordersbpm.domain.models.projections.order;


public interface OrderInfoForDelete {

    Long getId();

    boolean isActive();

    boolean isCancelable();

    boolean getStarted();

}
