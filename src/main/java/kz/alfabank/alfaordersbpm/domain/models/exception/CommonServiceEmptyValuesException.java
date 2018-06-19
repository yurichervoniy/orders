package kz.alfabank.alfaordersbpm.domain.models.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CommonServiceEmptyValuesException extends RuntimeException {
    public CommonServiceEmptyValuesException(String message) {
        super(message);
    }
}
