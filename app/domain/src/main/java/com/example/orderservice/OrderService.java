package com.example.orderservice;

import com.s2s.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
class OrderService {

    private final OrderManagement orderManagement;

    public Order createOrder() throws IOException, TimeoutException {
        final String userId = orderManagement.provideUserId();
        if (userId == null) throw new IllegalArgumentException("UserId is not exists!");

        String systemToken = "Bearer " + JwtUtil.generateToken("order-service", userId, "SYSTEM");
        Order orderWithCartDetails = orderManagement.fetchCartInfo(systemToken);
        if (orderWithCartDetails != null && checkIfOrderExistsForCart(orderWithCartDetails.getCartId())) {
            return processOrder(orderWithCartDetails.getCartId(), userId, orderWithCartDetails);
        } else {
            throw new IllegalArgumentException("Order for this cart already exists!");
        }
    }

    private boolean checkIfOrderExistsForCart(String cartId) {
        return !orderManagement.isOrderExistsForCart(cartId);
    }

    private Order processOrder(String cartId, String userId, Order orderWithCartDetails) throws IOException, TimeoutException {
        Order order = new Order(
                generateOrderId(),
                userId,
                cartId,
                LocalDateTime.now(),
                orderWithCartDetails.getProducts(),
                orderWithCartDetails.getTotalPrice(),
                OrderStatus.PROCESS
        );
        return orderManagement.createOrder(order);
    }

    private String generateOrderId() {
        String orderId = UUID.randomUUID().toString();
        if (orderManagement.isOrderIdAlreadyExists(orderId)) {
            generateOrderId();
        }
        return orderId;
    }
}
