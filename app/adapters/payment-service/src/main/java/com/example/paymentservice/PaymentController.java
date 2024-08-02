package com.example.paymentservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping
    ResponseEntity<PaymentInfoRecord> createPayment() throws IOException, TimeoutException {
        var response = paymentMapper.toPaymentInfoRecord(paymentService.createPayment());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
