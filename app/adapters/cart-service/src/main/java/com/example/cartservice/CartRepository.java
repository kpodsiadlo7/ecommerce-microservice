package com.example.cartservice;

import org.springframework.data.jpa.repository.JpaRepository;

interface CartRepository extends JpaRepository<CartEntity,Long> {
    boolean existsByUserIdAndStatus(String userId, CartStatus status);

    boolean existsByCartId(String cartId);

    CartEntity findByUserId(String userId);
}
