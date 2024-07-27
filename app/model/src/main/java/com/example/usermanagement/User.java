package com.example.usermanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;

@Data
@Getter
@AllArgsConstructor
public class User {
    private Long id;
    private String uniqueUserId;
    private String username;
    private String password;
    private Collection<Role> roles;
}
