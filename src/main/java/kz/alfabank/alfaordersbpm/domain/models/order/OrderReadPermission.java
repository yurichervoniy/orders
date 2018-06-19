package kz.alfabank.alfaordersbpm.domain.models.order;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#id, 'RetailOrder', 'read')")
public @interface OrderReadPermission {
}
