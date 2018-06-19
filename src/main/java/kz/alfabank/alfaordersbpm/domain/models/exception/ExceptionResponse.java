package kz.alfabank.alfaordersbpm.domain.models.exception;

import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public class ExceptionResponse {
    private final LocalDateTime timestamp;
    private final String status;
    private final String error;
    private final String message;
    private final String path;
    private final String details;


    private ExceptionResponse(LocalDateTime timestamp, String status, String error, String message, String details, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
        this.path = path;
    }

    private static String getExceptionMessage(Throwable t){
        return Optional.ofNullable(t.getMessage()).orElse(t.toString());
    }

    public static ExceptionResponse of(String status, String error, String message, String details, String path){
        return new ExceptionResponse(LocalDateTime.now(), status, error, message, details, path);
    }

    public static ExceptionResponse of(HttpStatus status, Exception ex, HttpServletRequest request){
        Optional<Throwable> rootCause = Stream.iterate(ex, Throwable::getCause).filter(element -> element.getCause() == null).findFirst();
        String message = rootCause.isPresent()? getExceptionMessage(rootCause.get()) : getExceptionMessage(ex);
        return of(Integer.toString(status.value()), status.getReasonPhrase(), message, ex.toString(), request.getRequestURI());
    }

    public static ExceptionResponse of(HttpStatus status, String error, Exception ex, HttpServletRequest request){
        return of(Integer.toString(status.value()), error, ex.getMessage(), request.getRequestURI(), request.getRequestURI());
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "timestamp=" + timestamp +
                ", status='" + status + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExceptionResponse that = (ExceptionResponse) o;

        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}