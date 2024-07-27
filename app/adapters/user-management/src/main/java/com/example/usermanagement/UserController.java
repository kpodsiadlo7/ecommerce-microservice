package com.example.usermanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor

public class UserController {

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

    @GetMapping("/private")
    public String authenticate() {
        return "ok";
    }
}
