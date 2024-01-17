package ru.gts.highload.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import ru.otus.highload.model.ApplicationError;

import java.util.List;


@ControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = {AuthenticationException.class, AuthorizationServiceException.class})
    public ResponseEntity<ApplicationError> onAuthException(AuthenticationException ex, HttpServletResponse response) {
        log.error("Authentication error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApplicationError()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message(ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ApplicationError> onUserNotFound(Exception ex, HttpServletResponse response) {
        log.error("Unknown error", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApplicationError()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                );
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApplicationError> onException(Exception ex, HttpServletResponse response) {
        log.error("Unknown error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApplicationError()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage())
                );
    }

    @ExceptionHandler({
            ServletRequestBindingException.class,
            MethodArgumentTypeMismatchException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            InvalidFormatException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class,
            ValidationException.class,
            IllegalArgumentException.class
    })
    @ResponseBody
    public ResponseEntity<ApplicationError> handleValidationErrors(Exception ex, HttpServletResponse response) {
        log.error("Validation error", ex);
        String message;
        if (ex instanceof MethodArgumentNotValidException) {
            message = getValidationErrorMessage((MethodArgumentNotValidException) ex);
        } else {
            message = ex.getMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApplicationError()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(message)
                );
    }

    /**
     * Формирует сообщение для ошибки валидации.
     *
     * @param exception общее исключение при валидации параметров
     * @return сформированное сообщение об ошибке
     */
    private String getValidationErrorMessage(MethodArgumentNotValidException exception) {
        String message;

        List<String> validationViolations = exception.getAllErrors()
                .stream()
                .map(error -> (error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName())
                        + ": " + error.getDefaultMessage())
                .toList();

        if (!CollectionUtils.isEmpty(validationViolations)) {
            message = String.join(", ", validationViolations);
        } else {
            message = exception.getMessage();
        }

        return message;
    }
}
