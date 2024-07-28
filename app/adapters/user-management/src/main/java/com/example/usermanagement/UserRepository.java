package com.example.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUniqueUserId(String uniqueUserId);

    boolean existsByUsername(String login);

    Optional<UserEntity> findByUsername(String username);

    UserEntity findByUniqueUserId(String uniqueUserId);
}
