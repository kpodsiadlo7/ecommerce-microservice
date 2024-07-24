package com.example.usermanagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalErrorsHandler {

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("unexpected exception {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorDto(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, NoResourceFoundException.class})
    public final ResponseEntity<?> handleBadRequestException(Exception ex) {
        log.warn("Argument exception {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorDto(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public final ResponseEntity<?> handleNotFoundException(Exception ex) {
        log.warn("Not found exception {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorDto(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}

