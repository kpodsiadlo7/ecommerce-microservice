package com.example.orderservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service", url = "${service-url.cart}")
interface CartClient {

    @GetMapping("/order/cart")
    CartInfoRecord fetchCartInfo(@RequestHeader("Authorization") String authorization);
}
