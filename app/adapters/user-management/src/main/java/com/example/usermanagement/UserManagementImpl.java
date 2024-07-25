package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
class UserManagementImpl implements UserManagement {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void registerUser(String login, String password) {
        User user = new User(
                generateUniqueUserId(),
                login,
                passwordEncoder.encode(password),
                Role.ROLE_USER);
        userRepository.save(user);
    }

    private String generateUniqueUserId() {
        String uniqueUserId = UUID.randomUUID().toString();
        if (userRepository.existsByUniqueUserId(uniqueUserId)) {
            generateUniqueUserId();
        }
        return uniqueUserId;
    }

    public ResponseEntity<String> processLogin(final String login, final String password) throws IOException {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            String uniqueUserId = userRepository.findByUsername(userDetails.getUsername()).getUniqueUserId();
            String jwt = jwtUtil.generateToken(uniqueUserId);
            return ResponseEntity.ok(jwt);
        } else {
            //return ResponseEntity.status(401).body("Authentication failed");
            throw new AuthenticationException("Authentication failed");
        }
    }
}
