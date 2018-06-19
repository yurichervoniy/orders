package kz.alfabank.alfaordersbpm.domain.models.sms;

import java.io.Serializable;
import java.util.Objects;

public final class SmsMessage implements Serializable {

    private final String phone;
    private final String text;
    private final String serviceName;
    private final String correlationId;

    private SmsMessage(String phone, String text, String serviceName, String correlationId) {
        this.phone = phone;
        this.text = text;
        this.serviceName = serviceName;
        this.correlationId = correlationId;
    }


    public static SmsMessage of(String phone, String text, String serviceName, String correlationId) {
        return new SmsMessage(phone, text, serviceName, correlationId);
    }

    public String getPhone() {
        return phone;
    }

    public String getText() {
        return text;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SmsMessage{");
        sb.append("phone='").append(phone).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", correlationId='").append(correlationId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsMessage that = (SmsMessage) o;
        return Objects.equals(phone, that.phone) &&
                Objects.equals(text, that.text) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(correlationId, that.correlationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, text, serviceName, correlationId);
    }
}
