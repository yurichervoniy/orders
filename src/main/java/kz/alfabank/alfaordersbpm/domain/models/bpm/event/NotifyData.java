package kz.alfabank.alfaordersbpm.domain.models.bpm.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonPropertyOrder({"type", "value"})
public class NotifyData {

    private static final String STRING_TYPE = "String"; //FOR BPM

    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private final String type;

    @JacksonXmlText
    private final String value;

    private NotifyData(String value) {
        this.value = value;
        this.type = STRING_TYPE;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static NotifyData of (String value){
        return new NotifyData(value);
    }
}
