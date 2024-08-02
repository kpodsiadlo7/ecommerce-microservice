package com.example.paymentservice;

import com.s2s.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentManager paymentManager;

    Payment createPayment() throws IOException, TimeoutException {
        final String userId = paymentManager.provideUserId();
        if (userId == null) throw new IllegalArgumentException("UserId is not exists!");
        log.info("Create payment for user {}", userId);

        String systemToken = "Bearer " + JwtUtil.generateToken("payment-service", userId, "SYSTEM");
        OrderInfo orderInfo = paymentManager.fetchOrderInfo(systemToken);

        if (orderInfo != null) {
            Payment payment = processPayment(orderInfo, userId);
            paymentManager.eventPaymentStatus(payment);
            return payment;
        } else {
            throw new IllegalArgumentException("Payment for this order already exists!");
        }
    }

    private Payment processPayment(OrderInfo orderInfo, String userId) {
        log.info("Process payment for {}", orderInfo.getOrderId());
        Payment newPayment = new Payment(
                generateOrderId(),
                orderInfo.getOrderId(),
                orderInfo.getCartId(),
                userId,
                orderInfo.getTotalPrice(),
                LocalDateTime.now(),
                finalizePayment()
        );
        return paymentManager.createPayment(newPayment);
    }

    private PaymentStatus finalizePayment() {
        return randomStatus();
    }

    private PaymentStatus randomStatus() {
        Random random = new Random();
        if (random.nextBoolean()) {
            return PaymentStatus.COMPLETE;
        } else {
            return PaymentStatus.FAILED;
        }
    }

    private String generateOrderId() {
        String paymentId = UUID.randomUUID().toString();
        if (paymentManager.isPaymentIdAlreadyExists(paymentId)) {
            generateOrderId();
        }
        return paymentId;
    }
}
