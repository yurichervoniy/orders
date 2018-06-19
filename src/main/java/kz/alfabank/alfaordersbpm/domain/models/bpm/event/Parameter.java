package kz.alfabank.alfaordersbpm.domain.models.bpm.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"key", "value"})
public class Parameter {

    @JacksonXmlProperty(localName = "key")
    private final String key;

    @JacksonXmlProperty(localName = "value")
    private final Object value;


    private Parameter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public static Parameter of(String key, Object value){
        return new Parameter(key, value);
    }
}
