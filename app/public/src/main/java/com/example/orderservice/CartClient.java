package com.example.orderservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service", url = "${cart-service.url}")
interface CartClient {

    @GetMapping("/order/cart")
    CartInfoRecord isCartExists(@RequestHeader("Authorization") String authorization);
}
