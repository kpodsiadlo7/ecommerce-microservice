package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

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
                                        @RequestParam String password) throws NoSuchAlgorithmException, IOException {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            String uniqueUserId = userRepository.findByUsername(userDetails.getUsername()).getUniqueUserId();
            String jwt = jwtUtil.generateToken(userDetails.getUsername(), uniqueUserId);
            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

    @GetMapping("/test")
    public UserRecord getUserTestSecurity() {
        return userMapper.mapToDto(userRepository.findByUsername("login"));
    }
}
