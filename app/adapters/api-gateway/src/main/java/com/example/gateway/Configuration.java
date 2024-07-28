package com.example.gateway;

import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private static final String USER_MANAGEMENT_KEY_PATH = "app/adapters/user-management/key.txt";
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("cart-service", null);
            S2SVerification.addToTrustedStore("eventbus", null);
            S2SVerification.addToTrustedStore("order-service", null);
            S2SVerification.addToTrustedStore("payment-service", null);
            S2SVerification.addToTrustedStore("product-catalog", null);
            S2SVerification.addToTrustedStore("profile-service", null);
            S2SVerification.addToTrustedStore("user-management", KeyProvider.provideKey(USER_MANAGEMENT_KEY_PATH));
        };
    }
}
