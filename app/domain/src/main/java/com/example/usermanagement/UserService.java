package com.example.usermanagement;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
class UserService {

    private final UserManagement userManagement;

    String processRegistration(String login, String password, String confirmPassword) throws IllegalArgumentException {
        String validationError = validateBeforeRegisterProcess(login, password, confirmPassword);
        if (validationError == null) {
            User user = new User(
                    null,
                    generateUniqueUserId(),
                    login,
                    password,
                    Collections.singleton(Role.ROLE_USER));
            userManagement.registerUser(user);
            return "User registered successful";
        }
        throw new IllegalArgumentException(validationError);
    }

    private String generateUniqueUserId() {
        String uniqueUserId = UUID.randomUUID().toString();
        if (userManagement.isIdAlreadyExists(uniqueUserId)) {
            generateUniqueUserId();
        }
        return uniqueUserId;
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
        if (userManagement.isUserAlreadyExists(login)) {
            return "User already exists!";
        }
        return null;
    }

    public String processLogin(final String login, final String password) throws IOException {
        return userManagement.processLogin(login, password);
    }
}
