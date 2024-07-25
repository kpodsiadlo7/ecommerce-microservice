package com.example.usermanagement;

import org.springframework.http.ResponseEntity;

import java.util.List;

interface UserRepository {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByUniqueUserId(String uniqueUserId);
    User save(User user);
    List<User> findAll();
    User findByUniqueUserId(String uniqueUserId);
}
