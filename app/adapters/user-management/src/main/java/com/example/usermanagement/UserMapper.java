package com.example.usermanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserEntity toEntity(User user) {
        return new UserEntity(
                null,
                user.getUniqueUserId(),
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getRoles()
        );
    }
}
