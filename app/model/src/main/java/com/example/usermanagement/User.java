package com.example.usermanagement;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String uniqueUserId;
    private String username;
    private String password;
    private Collection<Role> roles;
}
