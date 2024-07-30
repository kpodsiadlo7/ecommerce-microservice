package com.example.cartservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CartMapper {

    private final ProductMapper productMapper;

    CartEntity toEntity(Cart cart) {
        return new CartEntity(
                cart.getId(),
                cart.getCartId(),
                cart.getUserId(),
                cart.getTotalPrice(),
                cart.getStatus(),
                productMapper.toEntityList(cart.getProducts())
        );
    }

    Cart toDomain(CartEntity cartEntity) {
        return Cart.builder()
                .id(cartEntity.getId())
                .cartId(cartEntity.getCartId())
                .userId(cartEntity.getUserId())
                .totalPrice(cartEntity.getTotalPrice())
                .status(cartEntity.getStatus())
                .products(productMapper.toDomainList(cartEntity.getProducts()))
                .build();
    }

    CartRecord toRecord(Cart myCart) {
        return new CartRecord(
                myCart.getCartId(),
                myCart.getUserId(),
                productMapper.toRecordList(myCart.getProducts()),
                myCart.getTotalPrice()
        );
    }
}
