package kz.alfabank.alfaordersbpm.domain.service.smsconfirmation;

import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmation;
import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmationCheck;
import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmationResponse;
import kz.alfabank.alfaordersbpm.domain.models.phoneconfirm.SmsCodeConfirmation;
import kz.alfabank.alfaordersbpm.domain.models.sms.SmsMessage;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.SmsCodeConfirmationRepository;
import kz.alfabank.alfaordersbpm.domain.service.smsservice.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SmsConfirmationServiceImpl implements SmsConfirmationService {

    private static final Logger LOG = LoggerFactory.getLogger(SmsConfirmationServiceImpl.class);

    private final RetailOrderRepository retailOrderRepository;
    private final SmsCodeConfirmationRepository confirmationRepository;
    private final SmsService smsService;

    @Autowired
    public SmsConfirmationServiceImpl(RetailOrderRepository retailOrderRepository, SmsCodeConfirmationRepository confirmationRepository, SmsService smsService) {
        this.retailOrderRepository = retailOrderRepository;
        this.confirmationRepository = confirmationRepository;
        this.smsService = smsService;
    }

    @Override
    public PhoneConfirmationResponse sendSmsConfirmation(Long orderId, PhoneConfirmation phone) {
        LOG.debug("sendSmsConfirmation orderId={}= PhoneConfirmation={}", orderId, phone);
        if(!retailOrderRepository.existsById(orderId))
            throw new EmptyResultDataAccessException(String.format("Заявка с ID=%s не найдена для посылки кода подтверждения", orderId),1);
        String message = null;
        boolean flag = false;
        Optional<SmsCodeConfirmation> optional = confirmationRepository.findByPhoneAndOrderId(phone.getMobilePhone(), orderId);
        SmsCodeConfirmation confirmation = optional.orElse(SmsCodeConfirmation.of(orderId, phone));

        if (confirmation.getWaitDate()!= null && confirmation.getWaitDate().isAfter(LocalDateTime.now())) {
            message = "Время ожидания СМС еще не истекло, попробуйте позднее";
        } else
        if (confirmation.getSendCount() >= confirmation.getSendLimit()) {
            message = "Максимальное число попыток повторной отправки кода подтверждения превышено, заявка блокирована";
        }

        if(message == null){
            flag = true;
            confirmation.setConfirmationCode(String.valueOf(SmsCodeConfirmation.generateRandomValue()));
            confirmation.setSendCount(confirmation.getSendCount() + 1);
            confirmation.setExpireDate(LocalDateTime.now().plusMinutes(SmsCodeConfirmation.getExpireMinutes()));
            confirmation.setWaitDate(LocalDateTime.now().plusSeconds(SmsCodeConfirmation.getWaitSeconds()));
            confirmation.setMessage(String.format("%s Ваш код подтверждения заявки в Альфа-Банке",confirmation.getConfirmationCode()));
            confirmation.setPhone(phone.getMobilePhone());
            message = "СМС отправлено";
        }

        confirmation = confirmationRepository.saveAndFlush(confirmation);
        if (flag){
            smsService.sendSms(SmsMessage.of(confirmation.getPhone(), confirmation.getMessage(),"SmsConfirmation", confirmation.getId().toString()));
        }
        return new PhoneConfirmationResponse(confirmation.getId(),confirmation.getExpireDate(), confirmation.getSendCount(), confirmation.getSendLimit(), message, confirmation.getWaitDate());
    }

    @Override
    public PhoneConfirmationCheck checkSmsConfirmation(Long orderId, String phone, String code) {
        LOG.debug("checkSmsConfirmation orderId={} Phone={} Code={}",orderId, phone, code);
        if(orderId == null)
            throw new IllegalArgumentException("To perform checkSmsConfirmation orderId must not be null");
        if(phone == null || phone.isEmpty())
            throw new IllegalArgumentException("To perform checkSmsConfirmation phone must not be blank");
        if(code == null || code.isEmpty())
            throw new IllegalArgumentException("To perform checkSmsConfirmation code must not be blank");

        Optional<SmsCodeConfirmation> optional = confirmationRepository.findByPhoneAndOrderId(phone, orderId);
        if (!optional.isPresent()){
            return new PhoneConfirmationCheck(false,"Код подтверждения не найден");
        }
        SmsCodeConfirmation confirmation = optional.get();
        if (confirmation.getExpireDate().isBefore(LocalDateTime.now()))
            return new PhoneConfirmationCheck(false, "Код устарел");
        boolean isValid = confirmation.getConfirmationCode().equals(code);
        return new PhoneConfirmationCheck(isValid, isValid ? null : "Неверный код");
    }



}
