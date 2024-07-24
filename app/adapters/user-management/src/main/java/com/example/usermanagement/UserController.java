package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    String test() {
        return "login działa";
    }

    @GetMapping(value = "/register")
    ResponseEntity<?> register(@RequestParam final String login,
                               @RequestParam final String password,
                               @RequestParam final String confirmPassword) {
        return userService.processRegistration(login,password,confirmPassword);
    }
}
