package kz.alfabank.alfaordersbpm.domain.models.projections.order;

import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface MyRetailOrders {

    Long getId();

    String getOrderNumber();

    LocalDate getOrderDate();

    LocalDate getStartDate();

    CommonServiceRef getCreditProductRef();

    BigDecimal getRequestedAmount();

    Integer getLoanDuration();

    String getIin();

    String getLastName();

    String getFirstName();

    String getMiddleName();

    default Optional<String> getFullName() {
        int size = (getLastName()==null? 0 : getLastName().length())
                    + (getFirstName()==null? 0 : getFirstName().length()+1)
                    + (getMiddleName()==null? 0 : getFirstName().length()+1);
        StringBuilder sb = new StringBuilder(size);
        Optional.ofNullable(getLastName()).ifPresent(sb::append);
        Optional.ofNullable(getFirstName()).ifPresent(sb.append(" ")::append);
        Optional.ofNullable(getMiddleName()).ifPresent(sb.append(" ")::append);

        return Optional.ofNullable(sb.toString().trim());
    }

    boolean isActive();

    boolean isCancelable();

    String getStepUI();

    default String getStepName(){
        return getStepUI();
    }

    String getPiid();

}
