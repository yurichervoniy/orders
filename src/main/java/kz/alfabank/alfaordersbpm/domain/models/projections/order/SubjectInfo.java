package kz.alfabank.alfaordersbpm.domain.models.projections.order;

import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.Gender;

import java.time.LocalDate;
import java.util.Optional;

public interface SubjectInfo {

    String getIin();

    Optional<String> getLastName();

    Optional<String> getFirstName();

    Optional<String> getMiddleName();

    LocalDate getBirthDate();

    CommonServiceRef getGenderRef();

    Gender getGender();

}
