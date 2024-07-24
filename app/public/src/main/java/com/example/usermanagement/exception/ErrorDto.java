package com.example.usermanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorDto {
    private String message;
    private HttpStatus status;

    public ErrorDto(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
