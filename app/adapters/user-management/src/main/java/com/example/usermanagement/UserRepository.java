package com.example.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUniqueUserId(String uniqueUserId);

    boolean existsByUsername(String login);

    UserEntity findByUsername(String username);

    UserEntity findByUniqueUserId(String uniqueUserId);
}
