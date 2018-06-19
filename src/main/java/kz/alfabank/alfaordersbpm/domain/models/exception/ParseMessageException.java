package kz.alfabank.alfaordersbpm.domain.models.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ParseMessageException extends RuntimeException {

    private final String data;
    private final Class clazz;


    public ParseMessageException(String message, Throwable cause,  String data, Class clazz){
        super(message, cause);
        this.data = data;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return super.toString() + " ParseMessageException{" +
                "data='" + data + '\'' +
                ", clazz=" + clazz +
                '}';
    }
}
