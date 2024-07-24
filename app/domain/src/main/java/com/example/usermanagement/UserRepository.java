package com.example.usermanagement;

interface UserRepository {
    User findByUsername(String username);
}
