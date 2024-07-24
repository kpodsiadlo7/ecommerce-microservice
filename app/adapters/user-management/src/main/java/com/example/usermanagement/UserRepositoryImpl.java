package com.example.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepositoryImpl extends UserRepository, JpaRepository<User,Long> {

}
