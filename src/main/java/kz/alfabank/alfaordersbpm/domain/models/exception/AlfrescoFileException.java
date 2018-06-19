package kz.alfabank.alfaordersbpm.domain.models.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AlfrescoFileException extends RuntimeException{

    public AlfrescoFileException(String message) {
        super(message);
    }

    public AlfrescoFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
