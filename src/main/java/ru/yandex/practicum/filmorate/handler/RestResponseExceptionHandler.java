package ru.yandex.practicum.filmorate.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "Timestamp";
    private static final String STATUS = "Status";
    private static final String ERROR = "Error";
    private static final String PATH = "Path";
    private static final String REASONS = "Reasons";


    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        log.error("Несоответствие требованиям: {}", ex.getMessage(), ex);
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.BAD_REQUEST, request);
        List<String> errors = Arrays.stream(ex.getMessage().split(",")).collect(Collectors.toList());
        body.put(REASONS, errors);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final FilmNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowableException(final Throwable e) {
        return new ErrorResponse(e.getMessage());
    }


    private Map<String, Object> getGeneralErrorBody(HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, status.value());
        body.put(ERROR, status.getReasonPhrase());
        body.put(PATH, getRequestURI(request));
        return body;
    }


    private String getRequestURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
            return String.format("%s %s", httpRequest.getMethod(), httpRequest.getRequestURI());
        } else {
            return "";
        }
    }
}
