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

    void updateCartStatusAfterPayment(String eventId, String eventStatus) throws IOException, TimeoutException;

    Cart updateCart(Cart cart, List<EventProduct> products) throws IOException, TimeoutException;

    String provideUserId();

    Cart fetchCartForUser(String userId);

    void updateEventStatusAfterUnsresevedProducts(String eventId, String eventStatus);
}
