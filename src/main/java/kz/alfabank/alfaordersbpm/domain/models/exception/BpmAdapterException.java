package kz.alfabank.alfaordersbpm.domain.models.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BpmAdapterException extends RuntimeException  {

    public BpmAdapterException(String message) {
        super(message);
    }

    public BpmAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

}
