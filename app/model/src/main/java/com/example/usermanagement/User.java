package com.example.usermanagement;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uniqueUserId;
    private String username;
    private String password;

    User(String uniqueUserId, String username, String password, Role role){
        this.uniqueUserId = uniqueUserId;
        this.username = username;
        this.password = password;
        this.roles.add(role);
    }

    private Collection<Role> roles  = new HashSet<>();
}
