package com.example.orderservice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Order {
    private String orderId;
    private String customerId;
    private String cartId;
    private LocalDateTime orderDate;
    private List<Product> products;
    private BigDecimal totalPrice;
    private OrderStatus status;
}
