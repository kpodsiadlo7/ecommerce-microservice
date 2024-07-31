package com.example.orderservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByOrderId(String orderId);

    boolean existsByUserIdAndStatus(String cartId, OrderStatus status);

    boolean existsByCartId(String cartId);
}
