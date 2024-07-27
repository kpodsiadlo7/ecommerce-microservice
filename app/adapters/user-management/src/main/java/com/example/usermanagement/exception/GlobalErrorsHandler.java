package com.example.usermanagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalErrorsHandler {

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<ErrorRecord> handleException(Exception ex) {
        log.error("unexpected exception {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorRecord(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class})
    public final ResponseEntity<?> handleBadRequestException(Exception ex) {
        log.warn("Argument exception {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorRecord(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public final ResponseEntity<?> handleNotFoundException(Exception ex) {
        log.warn("Not found exception {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorRecord(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadCredentialsException.class, AuthenticationException.class})
    public final ResponseEntity<?> handleAuthorizationException(AuthenticationException ex) {
        log.warn("Authorization exception {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorRecord(ex.getMessage(), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public final ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        log.warn("Null at {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorRecord(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public final ResponseEntity<?> handleMissingParameter(MissingServletRequestParameterException ex) {
        log.warn("Missing parameter {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorRecord(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            stringBuilder.append(error.getDefaultMessage()).append("\n");
        });
        return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
    }
}

