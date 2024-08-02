package com.example.orderservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentInfo {
    private String orderId;
    private PaymentStatus status;
}
