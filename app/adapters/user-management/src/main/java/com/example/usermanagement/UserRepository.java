package com.example.usermanagement;

import java.util.List;

interface UserRepository {
    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUniqueUserId(String uniqueUserId);

    UserEntity save(UserEntity user);

    List<UserEntity> findAll();

    UserEntity findByUniqueUserId(String uniqueUserId);
}
