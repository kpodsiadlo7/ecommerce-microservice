package com.example.paymentservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderInfo {
    private String cartId;
    private String orderId;
    private BigDecimal totalPrice;
}
