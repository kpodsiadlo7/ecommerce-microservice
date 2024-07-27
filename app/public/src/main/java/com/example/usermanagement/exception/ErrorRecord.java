package com.example.usermanagement.exception;

import org.springframework.http.HttpStatus;

public record ErrorRecord(String message, HttpStatus status) {
}
