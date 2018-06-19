package kz.alfabank.alfaordersbpm.domain.models.projections.order;

import java.time.LocalDate;

public interface AlfrescoOrder {

    Long getId();

    String getOrderNumber();

    LocalDate getOrderDate();

    String getIin();

}
