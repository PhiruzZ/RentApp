package com.example.rentapp.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ExceptionBody {


    private String application_name;
    private String method_name;
    private String exception_name;
    private String status;
    private String message;
    private String timestamp;

    public ExceptionBody(String msg, String application_name, String method_name, String exception_name, String status) {
        this.message = msg;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss").withLocale(
                Locale.ENGLISH
        ));
        this.method_name = method_name;
        this.application_name = application_name;
        this.exception_name = exception_name;
        this.status = status;

    }

    public static ExceptionBody of(ResponseStatusException exception, String application_name, String method_name, String exception_name) {
        var status = exception.getStatusCode().toString();
        String message = exception.getMessage();
        var exceptionBody = new ExceptionBody(message, application_name, method_name, exception_name, status);
        return exceptionBody;

    }

    public static ExceptionBody of(Exception e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR.name();
        String msg = e.getMessage();

        var exceptionBody = new ExceptionBody(msg, application_name, method_name, exception_name, status);
        return exceptionBody;
    }

    public static ExceptionBody of(MethodArgumentTypeMismatchException e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.BAD_REQUEST.name();
        String msg = "Failed to convert value to required type "+ e.getRequiredType();
        return new ExceptionBody(msg, application_name, method_name, exception_name, status);
    }

    public static ExceptionBody of(MethodArgumentNotValidException e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.BAD_REQUEST.name();
        String msg = e.getFieldErrors().stream()
                .map(f -> StringUtils.capitalize(f.getDefaultMessage()))
                .collect(Collectors.toList()).toString();

        return new ExceptionBody(msg, application_name, method_name, exception_name, status);
    }

    public static ExceptionBody of(ObjectOptimisticLockingFailureException e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.TOO_MANY_REQUESTS.name();
        String msg = "too many request! please try again!";
        return new ExceptionBody(msg, application_name, method_name, exception_name, status);
    }

    public static ExceptionBody of(HttpMessageNotReadableException e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.BAD_REQUEST.name();

        Throwable mostSpecificCause = e.getMostSpecificCause();
        if (mostSpecificCause instanceof MismatchedInputException) {
            return new ExceptionBody("Can't deserialize object", application_name, method_name, exception_name, status);
        }


        InvalidFormatException invalidFormatException = (InvalidFormatException) e.getMostSpecificCause();
        Class<?> targetType = invalidFormatException.getTargetType();

        String simpleName = targetType.getSimpleName();
        String msg = invalidFormatException.getValue() + " can't be parsed to " + simpleName + ".";
        if (targetType.isEnum()) {
            String fields = Arrays.stream(targetType.getFields()).map(Field::getName).collect(Collectors.joining(", "));
            msg += simpleName + " must be one of this: " + fields;
        }

        return new ExceptionBody(msg, application_name, method_name, exception_name, status);
    }
}
