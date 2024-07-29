package com.example.usermanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestParam final String login,
                               @RequestParam final String password,
                               @RequestParam final String confirmPassword) throws IllegalArgumentException {
        var response = userService.processRegistration(login, password, confirmPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
        return ResponseEntity.ok(userService.processLogin(loginRequest));
    }

    @GetMapping("/profile")
    public String authenticate() {
        return "Only user can reach this endpoint";
    }

    @GetMapping("/check-user")
    public boolean checkUser(@RequestParam String token, @RequestHeader("Authorization") String authorization) {
        log.warn("Only system can reach this endpoint.");
        return true;
    }
}
