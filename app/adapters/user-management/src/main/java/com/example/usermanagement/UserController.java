package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    ResponseEntity<?> register(@RequestParam final String login,
                               @RequestParam final String password,
                               @RequestParam final String confirmPassword) throws IllegalArgumentException {
        return userService.processRegistration(login, password, confirmPassword);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam final String login,
                                        @RequestParam final String password) throws IOException {
        return userService.processLogin(login, password);
    }
}
