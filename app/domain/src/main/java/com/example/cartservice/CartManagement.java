package com.example.cartservice;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

interface CartManagement {
    boolean userHasCart(String userId, CartStatus status);

    Cart createCartForUser(Cart cart);

    Cart getUserCart(String userId, CartStatus status);

    boolean isCartIdAlreadyExists(String cartId);

    Product checkProductAndReserve(Long productId, Integer quantity, String authorization);

    Cart saveCart(Cart cart);

    Cart updateCart(Product productRequest, Cart cart);

    void updateEventStatus(String eventId, String eventStatus);

    Cart updateCart(Cart cart, List<EventProduct> products) throws IOException, TimeoutException;

    String provideUserId();

    Cart isCartForOrder(String userId);
}
