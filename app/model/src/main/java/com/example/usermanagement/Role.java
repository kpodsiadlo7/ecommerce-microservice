package com.example.usermanagement;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Data
public class Role {

    @Id
    private Long id;
    private String name;

    @ManyToMany
    private Collection<User> users = new HashSet<>();
}
