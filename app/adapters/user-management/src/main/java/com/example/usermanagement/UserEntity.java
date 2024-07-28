package com.example.usermanagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@RequiredArgsConstructor
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uniqueUserId;
    private String username;
    private String password;
    private Collection<Role> roles = new HashSet<>();

    UserEntity(Long id, String uniqueUserId, String username, String password, Collection<Role> roles) {
        this.id = id;
        this.uniqueUserId = uniqueUserId;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
