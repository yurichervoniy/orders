package kz.alfabank.alfaordersbpm.domain.entitylisteners;

import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.aspectj.ConfigurableObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Configurable
public class OrderEntityListener implements ConfigurableObject {

    @PrePersist
    public void onPrePersist(AbstractOrder o) {
        // Interceptor for OrderEntityListener
        if (o.getOrderDate() == null)
            o.setOrderDate(LocalDate.now());
        if (o.getCorrelationId() == null)
            o.setCorrelationId(UUID.randomUUID().toString());
    }

    @PostPersist
    public void onPostPersist(AbstractOrder o) {
        // Interceptor for OrderEntityListener
    }

    @PostLoad
    public void onPostLoad(AbstractOrder o) {
        // Interceptor for OrderEntityListener
    }

    @PreUpdate
    public void onPreUpdate(AbstractOrder o) {
        // Interceptor for OrderEntityListener
    }

    @PostUpdate
    public void onPostUpdate(AbstractOrder o) {
        // Interceptor for OrderEntityListener
    }

    @PreRemove
    public void onPreRemove(AbstractOrder o) {
        // Interceptor for OrderEntityListener
    }

    @PostRemove
    public void onPostRemove(AbstractOrder o) {
        // Interceptor for OrderEntityListener
    }

}
