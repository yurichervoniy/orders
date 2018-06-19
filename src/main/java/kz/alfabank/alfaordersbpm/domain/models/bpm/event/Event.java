package kz.alfabank.alfaordersbpm.domain.models.bpm.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;


@JsonPropertyOrder({"processApp", "ucaName", "eventId"})
public class Event {

    private static final String PROCESS_APP = "SCD_BPM";
    private static final String UCA_NAME ="UCA_PIL_TERMINATE";
    private static final String EVENT_ID = "3afb4708-0014-4b45-bacf-0bcterminate";

    @JacksonXmlProperty(isAttribute = true, localName = "processApp")
    private final String processApp;

    @JacksonXmlProperty(isAttribute = true, localName = "ucaname")
    private final String ucaName;

    @JacksonXmlText
    private final String eventId;

    private Event (){
        this.processApp = PROCESS_APP;
        this.ucaName = UCA_NAME;
        this.eventId = EVENT_ID;
    }

    public String getProcessApp() {
        return processApp;
    }

    public String getUcaName() {
        return ucaName;
    }

    public String getEventId() {
        return eventId;
    }

    public static Event of(){
        return new Event();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("processApp='").append(processApp).append('\'');
        sb.append(", ucaName='").append(ucaName).append('\'');
        sb.append(", eventId='").append(eventId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
