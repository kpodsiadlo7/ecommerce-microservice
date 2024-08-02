package com.example.orderservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service", url = "${service-url.payment}")
public interface PaymentClient {

    @PostMapping
    PaymentInfo createPaymentForOrder(@RequestHeader("Authorization") String authorization);
}
