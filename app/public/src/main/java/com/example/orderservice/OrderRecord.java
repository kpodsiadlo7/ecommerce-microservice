package com.example.orderservice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderRecord(String orderId,
                          String customerId,
                          String cartId,
                          LocalDateTime orderDate,
                          List<ProductRecord> products,
                          BigDecimal totalAmount,
                          OrderStatus status) {
}