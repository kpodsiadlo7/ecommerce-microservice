package com.example.productcatalog.exception;

import com.example.usermanagement.exception.ErrorRecord;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalErrorsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            stringBuilder.append(error.getDefaultMessage()).append("\n");
        });
        log.error(stringBuilder.toString());
        return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public final ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Missing parameter {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorRecord("Required request body is missing or invalid", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public final ResponseEntity<?> handleNotFoundException(EntityNotFoundException ex) {
        log.warn("Not found exception {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorRecord(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}
