package com.example.usermanagement;

public interface RoleRepository {
    Role findByName(String name);
}
