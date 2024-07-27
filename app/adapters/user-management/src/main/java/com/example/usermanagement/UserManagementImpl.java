package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void registerUser(User user) {
        UserEntity userToSave = userMapper.toEntity(user);
        userRepository.save(userToSave);
    }

    public String processLogin(final String login, final String password) throws IOException {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            String uniqueUserId = userRepository.findByUsername(userDetails.getUsername()).getUniqueUserId();
            return jwtUtil.generateToken(uniqueUserId);
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
