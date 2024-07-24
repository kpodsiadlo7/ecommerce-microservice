package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/register")
    ResponseEntity<?> register(@RequestParam final String login,
                               @RequestParam final String password,
                               @RequestParam final String confirmPassword) throws IllegalArgumentException {
        return userService.processRegistration(login, password, confirmPassword);
    }

    @GetMapping("/users")
    List<UserRecord> getAllUsers() {
        return userMapper.mapToDtoList(userRepository.findAll());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String login,
                                        @RequestParam String password) {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse.isAuthenticated()) {
            return ResponseEntity.ok("User authenticated");
        } else {
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }
}
