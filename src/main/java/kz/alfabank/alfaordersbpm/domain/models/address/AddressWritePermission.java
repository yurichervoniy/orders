package kz.alfabank.alfaordersbpm.domain.models.address;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#orderId, 'OrderAddress', 'write')")
public @interface AddressWritePermission {
}
