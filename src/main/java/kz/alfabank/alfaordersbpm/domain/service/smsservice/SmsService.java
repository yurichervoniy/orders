package kz.alfabank.alfaordersbpm.domain.service.smsservice;

import kz.alfabank.alfaordersbpm.domain.models.sms.SmsMessage;

public interface SmsService {

    void sendSms(SmsMessage smsMessage);

}
