package kz.alfabank.alfaordersbpm.domain.service.smsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.models.sms.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SmsServiceImpl implements SmsService {

    private final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SmsServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendSms(SmsMessage smsMessage) {
        if (smsMessage == null)
            throw new IllegalArgumentException("To send SMS, smsMessage mus not be null");
        logger.debug("Sending SMS message to RabbitMq. {}",smsMessage);
        ObjectMapper mapper = new ObjectMapper();
        String message = null;
        try {
            message = mapper.writeValueAsString(smsMessage);
        } catch (JsonProcessingException e) {
            throw new InternalServerException(String.format("Faild to transform SmsMessage to JSON %s", e.getMessage()), e);
        }
        rabbitTemplate.convertAndSend("sms-exchange", "inboundsms", message);
    }
}
