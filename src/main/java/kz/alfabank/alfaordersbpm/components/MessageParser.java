package kz.alfabank.alfaordersbpm.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.Message;

public interface MessageParser {

    <T> T tryParseMessage(final String message, Class<T> valueType);

    String writeValueAsString(Object value) throws JsonProcessingException;

    Message createMessage(Object value) throws JsonProcessingException;

}
