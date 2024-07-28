package com.example.cartservice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CartService {

    private final CartManagement cartManagement;

    private boolean userHasCart(String userId) {
        return cartManagement.userHasCart(userId);
    }
}
