package com.example.paymentservice;

import org.springframework.stereotype.Service;

@Service
class PaymentMapper {

    PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(
                null, // ID encji jest generowane przez bazę danych
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getCartId(),
                payment.getUserId(),
                payment.getTotalPrice(),
                payment.getPaymentTime(),
                payment.getStatus()
                );
    }

    Payment toDomain(PaymentEntity paymentEntity) {
        return new Payment(
                paymentEntity.getPaymentId(),
                paymentEntity.getOrderId(),
                paymentEntity.getCartId(),
                paymentEntity.getUserId(),
                paymentEntity.getTotalPrice(),
                paymentEntity.getPaymentTime(),
                paymentEntity.getStatus()
        );
    }

    public PaymentInfoRecord toPaymentInfoRecord(Payment payment) {
        return new PaymentInfoRecord(
                payment.getOrderId(),
                payment.getStatus()
        );
    }
}
