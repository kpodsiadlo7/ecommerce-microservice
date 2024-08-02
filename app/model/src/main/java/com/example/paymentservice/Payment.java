package com.example.paymentservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
class Payment {
    private String paymentId;
    private String orderId;
    private String cartId;
    private String userId;
    private BigDecimal totalPrice;
    private LocalDateTime paymentTime;
    private PaymentStatus status;
}
