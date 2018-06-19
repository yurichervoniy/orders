package kz.alfabank.alfaordersbpm.domain.service.signfinal;

import kz.alfabank.alfaordersbpm.domain.models.dto.SignFinalDTO;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.models.signfinal.SignFinal;

import java.util.Optional;

public interface SignFinalService {
    SignFinal getByOrderId(Long orderId);

    Optional<SignFinal> getOne(Long id);

    SignFinal save(SignFinal entity);

    SignFinal insert(SignFinal entity, RetailOrderUiStep step);

    SignFinal update(SignFinal entity);

    boolean existsById(Long id);

    SignFinal createLoanAgreement(SignFinalDTO signFinalDTO);

    void saveSignFinal(Long orderId);
}
