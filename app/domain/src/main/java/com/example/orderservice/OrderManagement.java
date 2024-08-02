package com.example.orderservice;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

interface OrderManagement {
    Order fetchCartInfo(String systemToken);

    Order createOrder(Order order) throws IOException, TimeoutException;

    boolean isOrderIdAlreadyExists(String orderId);

    String provideUserId();

    boolean isOrderExistsForCart(String cartId);

    void updateOrderStatus(PaymentInfo paymentInfo);

    OrderInfo fetchOrderInfo(String userId);
}
