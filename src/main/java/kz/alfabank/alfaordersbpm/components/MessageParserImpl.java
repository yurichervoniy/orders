package kz.alfabank.alfaordersbpm.components;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.alfabank.alfaordersbpm.domain.models.exception.ParseMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class MessageParserImpl implements MessageParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParserImpl.class);

    private static final String APP_ID = "ORDERSBPM";

    private final ObjectMapper mapper;

    @Autowired
    public MessageParserImpl(ObjectMapper mapper) {
        this.mapper = mapper;
        this.mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public <T> T tryParseMessage(String message, Class<T> valueType) {
        LOGGER.trace("tryParseMessage[{}] to Class[{}]", message, valueType.getSimpleName());
        T response;
        try {
            response = mapper.readValue(message, valueType);
        } catch (IOException e) {
            String s = String.format("Error in parsing [%s] to Class %s exception %s", message, valueType.getSimpleName(), e.getMessage());
            LOGGER.error(s, e);
            throw new ParseMessageException(s, e, message, valueType);
        }
        LOGGER.trace("Parsed MessageToClass {}", response);
        return response;
    }

    @Override
    public String writeValueAsString(Object value) throws JsonProcessingException {
        LOGGER.trace("writeValueAsString from Class[{}] value=[{}]", value.getClass().getSimpleName(), value);
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error in writeValueAsString", e);
            throw e;
        }
    }

    @Override
    public Message createMessage(Object value) throws JsonProcessingException {
        LOGGER.trace("createMessage from Class[{}] value=[{}]", value.getClass().getSimpleName(), value);
        try {
            byte[] body = mapper.writeValueAsBytes(value);
            return MessageBuilder.withBody(body)
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setContentEncoding(JsonEncoding.UTF8.getJavaName())
                    .setContentLength((long) body.length)
                    .setAppId(APP_ID)
                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                    .setTimestamp(new Date())
                    .build();
        }
        catch (Exception ex){
            LOGGER.error("Error in createMessage", ex);
            throw ex;
        }
    }

}
