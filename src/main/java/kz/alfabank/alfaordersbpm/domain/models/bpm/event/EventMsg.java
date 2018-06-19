package kz.alfabank.alfaordersbpm.domain.models.bpm.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "eventmsg")
@JsonPropertyOrder({"event", "parameters"})
public class EventMsg {

    private static final String CORRELATION_ID = "correlationId";
    private static final String NOTIFY_MESSAGE = "notifyMessage";

    @JacksonXmlProperty(localName = "event")
    private final Event event;

    @JacksonXmlElementWrapper(localName = "parameters")
    @JacksonXmlProperty(localName = "parameter")
    private final List<Parameter> parameters = new ArrayList<>(2);

    private EventMsg (AbstractOrder order){
        this.event = Event.of();
        NotifyMessage message = NotifyMessage.of(order);
        Parameter correlationId = Parameter.of(CORRELATION_ID, order.getPiid());
        Parameter notifyMessage = Parameter.of(NOTIFY_MESSAGE, message);
        parameters.add(correlationId);
        parameters.add(notifyMessage);
    }

    public static EventMsg of (AbstractOrder order){
        return new EventMsg(order);
    }

    public Event getEvent() {
        return event;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventMsg{");
        sb.append("event=").append(event);
        sb.append(", parameters=").append(parameters);
        sb.append('}');
        return sb.toString();
    }
}
