package com.example.usermanagement;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "\"user\"")
@Data
public class User {

    @Id
    private Long id;
    private String uniqueUserId;
    private String username;
    private String password;

    @Getter
    @ManyToMany
    private static Collection<Role> roles  = new HashSet<>();

}
