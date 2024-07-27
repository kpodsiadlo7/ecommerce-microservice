package com.example.usermanagement;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
class UserService {

    private final UserManagement userManagement;

    String processRegistration(String login, String password, String confirmPassword) throws IllegalArgumentException {
        System.out.println("processRegistration");
        String validationError = validateBeforeRegisterProcess(login, password, confirmPassword);
        if (validationError == null) {
            System.out.println("validationError == null");
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
        System.out.println("validateBeforeRegisterProcess");
        if (!password.equals(confirmPassword)) {
            return "Password and confirmation password do not match.";
        }
        if (userManagement.isUserAlreadyExists(login)) {
            return "User already exists!";
        }
        return null;
    }

    public String processLogin(final LoginRequest loginRequest) throws IOException {
        return userManagement.processLogin(loginRequest);
    }
}
