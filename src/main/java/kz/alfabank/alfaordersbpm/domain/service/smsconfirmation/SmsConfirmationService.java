package kz.alfabank.alfaordersbpm.domain.service.smsconfirmation;

import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmation;
import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmationCheck;
import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmationResponse;

public interface SmsConfirmationService {

    PhoneConfirmationResponse sendSmsConfirmation(Long orderId, PhoneConfirmation phone);


    PhoneConfirmationCheck checkSmsConfirmation(Long orderId, String phone, String code);
}
