package com.example.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryImpl extends RoleRepository, JpaRepository<Role, Long> {
}
