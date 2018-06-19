package kz.alfabank.alfaordersbpm.components;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface XmlMapper {

    String createMessage(Object value) throws JsonProcessingException;

}
