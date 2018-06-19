package kz.alfabank.alfaordersbpm.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class XmlMapperImpl implements kz.alfabank.alfaordersbpm.components.XmlMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParserImpl.class);

    private final XmlMapper mapper = new XmlMapper();

    @Override
    public String createMessage(Object value) throws JsonProcessingException{
        LOGGER.debug("createMessage as XML from Class[{}] value=[{}]", value.getClass().getSimpleName(), value);
        try {
            return mapper.writeValueAsString(value);
        }
        catch (Exception ex){
            LOGGER.error("Error in createMessage as XML", ex);
            throw ex;
        }
    }

}
