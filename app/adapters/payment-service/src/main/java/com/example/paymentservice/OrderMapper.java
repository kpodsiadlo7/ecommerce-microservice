package com.example.paymentservice;

import org.springframework.stereotype.Service;

@Service
class OrderMapper {

    OrderInfo fromRecord(OrderInfoRecord record){
        return new OrderInfo(
                record.cartId(),
                record.orderId(),
                record.totalPrice()
        );
    }
}
