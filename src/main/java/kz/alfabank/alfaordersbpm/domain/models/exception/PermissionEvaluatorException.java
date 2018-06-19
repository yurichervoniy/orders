package kz.alfabank.alfaordersbpm.domain.models.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PermissionEvaluatorException extends RuntimeException {

    public PermissionEvaluatorException(String message) {
        super(message);
    }

    public PermissionEvaluatorException(String message, Throwable cause) {
        super(message, cause);
    }

}
