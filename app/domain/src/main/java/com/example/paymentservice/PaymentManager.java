package com.example.paymentservice;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

interface PaymentManager {
    String provideUserId();

    OrderInfo fetchOrderInfo(String systemToken);

    boolean isPaymentIdAlreadyExists(String orderId);

    Payment createPayment(Payment newPayment);

    void eventPaymentStatus(Payment payment) throws IOException, TimeoutException;
}
