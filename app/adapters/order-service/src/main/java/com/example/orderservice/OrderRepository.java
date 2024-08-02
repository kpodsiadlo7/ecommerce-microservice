package com.example.orderservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByOrderId(String orderId);

    boolean existsByCartId(String cartId);

    Optional<OrderEntity> findByOrderId(String orderId);

    Optional<OrderEntity> findByUserIdAndStatus(String userId, OrderStatus orderStatus);
}
