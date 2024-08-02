package com.example.paymentservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    boolean existsByOrderId(String orderId);

    boolean existsByPaymentId(String paymentId);
}
