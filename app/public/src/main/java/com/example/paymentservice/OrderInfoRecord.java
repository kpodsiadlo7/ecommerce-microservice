package com.example.paymentservice;

import java.math.BigDecimal;

record OrderInfoRecord(String cartId, String orderId, BigDecimal totalPrice) {
}
