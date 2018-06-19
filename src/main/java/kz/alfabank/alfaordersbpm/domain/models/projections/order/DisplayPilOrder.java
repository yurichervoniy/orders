package kz.alfabank.alfaordersbpm.domain.models.projections.order;

import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.Gender;
import kz.alfabank.alfaordersbpm.domain.models.order.LoanPeriod;
import kz.alfabank.alfaordersbpm.domain.models.order.PaymentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface DisplayPilOrder {

    Long getId();

    String getOrderNumber();

    LocalDateTime getCreatedDate();

    String getCreatedBy();

    LocalDateTime getModifiedDate();

    String getModifiedBy();

    LocalDate getOrderDate();

    LocalDate getStartDate();

    CommonServiceRef getCreditProductRef();

    BigDecimal getRate();

    BigDecimal getRequestedAmount();

    CommonServiceRef getPaymentTypeRef();

    PaymentType getPaymentType();

    CommonServiceRef getInsuranceRef();

    BigDecimal getInsuracneAmount();

    BigDecimal getRegularPayment();

    Integer getLoanDuration();

    LoanPeriod getLoanPeriod();

    BigDecimal getAdditionalIncome();

    BigDecimal getInitialPayment();

    String getIin();

    Optional<String> getLastName();

    Optional<String> getFirstName();

    Optional<String> getMiddleName();

    default String getFullName() {
        StringBuilder sb = new StringBuilder();
        getLastName().ifPresent(sb::append);
        getFirstName().ifPresent(sb.append(" ")::append);
        getMiddleName().ifPresent(sb.append(" ")::append);
        return sb.toString().trim();
    }

    LocalDate getBirthDate();

    CommonServiceRef getGenderRef();

    Gender getGender();

    boolean isActive();

    boolean isCancelable();

    String getAttractedName();

    String getAttractedNameId();

    String getStepUI();

}
