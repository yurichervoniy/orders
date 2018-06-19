package kz.alfabank.alfaordersbpm.domain.models.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    String eventName() default "";

    String[] objectNames() default {};

    String[] changesDescription() default {};

    String[] securityMarks() default {};

}
