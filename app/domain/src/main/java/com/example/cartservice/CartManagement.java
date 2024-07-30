package com.example.cartservice;

interface CartManagement {
    boolean userHasCart(String userId, CartStatus status);

    Cart createCartForUser(Cart cart);

    Cart getUserCart(String userId);

    boolean isCartIdAlreadyExists(String cartId);

    Product checkProductAndReserve(Long productId, Integer quantity, String authorization);

    void saveProcessCart(Cart cart);

    Cart updateCart(Product productRequest, Cart cart, CartStatus status);

    void changeCartStatusToFailed(Cart cart, CartStatus cartStatus);
}
