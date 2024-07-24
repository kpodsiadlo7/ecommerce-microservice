package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
class UserManagementImpl implements UserManagement {

    private static final Logger log = LoggerFactory.getLogger(UserManagementImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerUser(String login, String password) {

        User user = new User(
                generateUniqueUserId(),
                login,
                passwordEncoder.encode(password),
                Role.ROLE_USER);
        userRepository.save(user);

        log.info(Role.ROLE_USER.name());
    }

    private String generateUniqueUserId() {
        String uniqueUserId = UUID.randomUUID().toString();
        if (userRepository.existsByUniqueUserId(uniqueUserId)) {
            generateUniqueUserId();
        }
        return uniqueUserId;
    }
}
