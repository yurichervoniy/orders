package kz.alfabank.alfaordersbpm.rest.exception;

import kz.alfabank.alfaordersbpm.domain.models.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleInternalServerException(Exception ex, HttpServletRequest request) {
        LOG.error("RestControllerAdvice->handleInternalServerException", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status, ex, request);
        return new ResponseEntity(exceptionResponse, status);
    }

    @ExceptionHandler(BpmAdapterException.class)
    public final ResponseEntity<ExceptionResponse> handleBpmAdapterException(BpmAdapterException ex, HttpServletRequest request) {
        LOG.error("RestControllerAdvice->handleBpmAdapterException", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status, BpmAdapterException.class.getSimpleName(), ex, request);
        return new ResponseEntity(exceptionResponse, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        LOG.error("RestControllerAdvice->handleIllegalArgumentException", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status, ex, request);
        return new ResponseEntity(exceptionResponse, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        LOG.error("RestControllerAdvice->handleAccessDeniedException", ex);
        HttpStatus status = HttpStatus.FORBIDDEN;
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status.toString(),AccessDeniedException.class.getSimpleName(), "Доступ запрещен", ex.toString(), request.getRequestURI());
        return new ResponseEntity(exceptionResponse, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        LOG.error("RestControllerAdvice->handleEntityNotFoundException", ex);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status, EntityNotFoundException.class.getSimpleName(), ex, request);
        return new ResponseEntity(exceptionResponse, status);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        LOG.error("RestControllerAdvice->handleBadRequestException", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status, ex, request);
        return new ResponseEntity(exceptionResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        LOG.error("RestControllerAdvice->handleMethodArgumentNotValidException", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        final StringBuilder sb = new StringBuilder();
        if (fieldErrors != null)
            fieldErrors.stream().forEach(e-> sb.append(e.getDefaultMessage()).append(" для поля '").append(e.getField()).append("': rejected value [").append(ObjectUtils.nullSafeToString(e.getRejectedValue() + "]; ")));
        String message = String.format("Ошибка валидации для объекта=[%s]. %s", ex.getBindingResult().getObjectName(), sb);
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status.toString(),"Validation Failed", message, ex.toString(), request.getRequestURI());
        return new ResponseEntity(exceptionResponse, status);
    }

}
