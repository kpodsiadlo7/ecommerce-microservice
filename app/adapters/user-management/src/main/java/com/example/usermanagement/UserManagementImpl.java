package com.example.usermanagement;

import com.s2s.JwtUtil;
import com.s2s.KeyProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
class UserManagementImpl implements UserManagement {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    @Value("${key.path}")
    private String keyPath;

    @Override
    @Transactional
    public void registerUser(User user) {
        log.info("registerUser");
        UserEntity userToSave = userMapper.toEntity(user);
        userRepository.save(userToSave);
    }

    public String processLogin(final LoginRequest loginRequest) throws IOException {
        String login = loginRequest.login();
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(login, loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            String uniqueUserId = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userDetails.getUsername())).getUniqueUserId();
            log.warn("UniqueUserId {}",uniqueUserId);
            return JwtUtil.generateToken("user-management", uniqueUserId, KeyProvider.provideKey(keyPath), "USER");
        } else {
            throw new AuthenticationException("Authentication failed");
        }
    }

    @Override
    public boolean isIdAlreadyExists(String uniqueUserId) {
        return userRepository.existsByUniqueUserId(uniqueUserId);
    }

    @Override
    public boolean isUserAlreadyExists(String login) {
        return userRepository.existsByUsername(login);
    }
}
