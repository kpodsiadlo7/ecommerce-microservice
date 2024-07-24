package com.example.usermanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public ResponseEntity<?> processRegistration(String login, String password, String confirmPassword) {
        String validationError = validateBeforeProcess(login, password, confirmPassword);
        if (validationError != null) {
            //process
        }
        return new ResponseEntity<>(new ErrorResponse(validationError), HttpStatus.BAD_REQUEST);
    }

    private String validateBeforeProcess(String login, String password, String confirmPassword) {
        if (login == null || password == null || confirmPassword == null) {
            return "Login, password, and confirmation password cannot be null.";
        }
        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Login, password, and confirmation password cannot be empty.";
        }
        if (password.length() > 16 || confirmPassword.length() > 16) {
            return "Password must be maximum 16 characters long.";
        }
        if (!password.equals(confirmPassword)) {
            return "Password and confirmation password do not match.";
        }
        return null;
    }
}
