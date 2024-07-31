package com.example.orderservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping("/create")
    ResponseEntity<OrderRecord> createOrder() throws IOException, TimeoutException {
        var response = orderMapper.toRecord(orderService.createOrder());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
