package com.example.cartservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-catalog", url = "${product-catalog.url}")
interface ProductClient {

    @GetMapping("/product/availability")
    ProductRecord checkAvailabilityProduct(@RequestParam Long productId,
                                           @RequestParam(required = false) Integer quantity,
                                           @RequestHeader("Authorization") String authorization);
}
