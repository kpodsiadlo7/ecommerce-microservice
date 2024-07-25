package com.example.usermanagement;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

interface UserManagement {
    void registerUser(final String login, final String password);

    ResponseEntity<String> processLogin(final String login, final String password) throws IOException;
}
