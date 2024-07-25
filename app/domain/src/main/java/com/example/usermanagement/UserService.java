package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserManagement userManagement;


    ResponseEntity<?> processRegistration(String login, String password, String confirmPassword) throws IllegalArgumentException {
        String validationError = validateBeforeRegisterProcess(login, password, confirmPassword);
        if (validationError == null) {
            userManagement.registerUser(login,password);
            return ResponseEntity.ok("User registered successful.");
        }
        throw new IllegalArgumentException(validationError);
    }

    private String validateBeforeRegisterProcess(String login, String password, String confirmPassword) {
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
        if (userRepository.existsByUsername(login)) {
            return "User already exists!";
        }
        return null;
    }

    public ResponseEntity<String> processLogin(final String login, final String password) throws IOException {
        return userManagement.processLogin(login,password);
    }
}
