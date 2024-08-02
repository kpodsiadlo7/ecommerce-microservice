package com.example.cartservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface CartRepository extends JpaRepository<CartEntity,Long> {
    boolean existsByUserIdAndStatus(String userId, CartStatus status);

    boolean existsByCartId(String cartId);

    CartEntity findByUserId(String userId);

    Optional<CartEntity> findByUserIdAndStatus(String userId, CartStatus cartStatus);

    Optional<CartEntity> findByCartIdAndStatus(String eventId, CartStatus cartStatus);
}
