package com.example.orderservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
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

    @GetMapping("/payment")
    ResponseEntity<OrderInfoRecord> fetchOrderInfoForPayment(){
        var response = orderMapper.toOrderInfoRecord(orderService.fetchOrder());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
