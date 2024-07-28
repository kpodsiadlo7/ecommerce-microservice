package com.example.cartservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class CartManagementImpl implements CartManagement {

    private final CartRepository cartRepository;

    @Override
    public boolean userHasCart(String userId) {
        return cartRepository.existsByUserId(userId);
    }
}
