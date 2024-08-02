package com.example.paymentservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "order-service", url = "${service-url.order}")
interface OrderClient {

    @GetMapping("/order/payment")
    OrderInfoRecord fetchCartInfo(@RequestHeader("Authorization") String authorization);
}
