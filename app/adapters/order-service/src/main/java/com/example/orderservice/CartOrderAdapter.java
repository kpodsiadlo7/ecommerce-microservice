package com.example.orderservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CartOrderAdapter {

    private final ProductMapper productMapper;

    Order toOrder(CartInfoRecord cartInfoRecord) {
        return Order.builder()
                .cartId(cartInfoRecord.cartId())
                .products(productMapper.toDomainList(cartInfoRecord.products()))
                .totalPrice(cartInfoRecord.totalPrice()).build();
    }
}
