package com.example.cartservice;

import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor
@org.springframework.context.annotation.Configuration
public class Configuration {

    private static final String API_GATEWAY_KEY_PATH = "app/adapters/api-gateway/key.txt";
    private static final String CART_SERVICE_KEY_PATH = "app/adapters/cart-service/key.txt";
    private final CartRepository cartRepository;
    private final EventListener eventListener;

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("gateway", KeyProvider.provideKey(API_GATEWAY_KEY_PATH));
            S2SVerification.addToTrustedStore("cart-service", KeyProvider.provideKey(CART_SERVICE_KEY_PATH));
            log.info("Rekordów w db {}", cartRepository.count());
            eventListener.listenerOnEvents();
        };
    }

    @Bean
    CartService cartService(CartManagement cartManagement){
        return new CartService(cartManagement);
    }
}
