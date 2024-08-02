package com.example.orderservice;

import java.math.BigDecimal;

public record OrderInfoRecord(String cartId, String orderId, BigDecimal totalPrice) {
}
