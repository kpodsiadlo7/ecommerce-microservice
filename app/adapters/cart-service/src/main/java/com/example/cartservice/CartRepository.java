package com.example.cartservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity,Long> {
    boolean existsByUserId(String userId);
}
