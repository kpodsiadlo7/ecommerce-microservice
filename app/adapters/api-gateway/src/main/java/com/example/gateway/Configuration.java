package com.example.gateway;

import com.s2s.S2SVerification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("cart-service", null);
            S2SVerification.addToTrustedStore("eventbus", null);
            S2SVerification.addToTrustedStore("order-service", null);
            S2SVerification.addToTrustedStore("payment-service", null);
            S2SVerification.addToTrustedStore("product-catalog", null);
            S2SVerification.addToTrustedStore("profile-service", null);
            S2SVerification.addToTrustedStore("user-management", null);
        };
    }
}
