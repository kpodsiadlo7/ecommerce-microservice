package com.example.usermanagement;

import java.io.IOException;

interface UserManagement {
    void registerUser(User user);

    String processLogin(final LoginRequest loginRequest) throws IOException;

    boolean isIdAlreadyExists(String uniqueUserId);

    boolean isUserAlreadyExists(String login);
}
