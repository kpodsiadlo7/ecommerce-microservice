package com.example.orderservice;

import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@org.springframework.context.annotation.Configuration
public class Configuration {

    //TODO wynieśc wszystkie takie path do jednego miejsca
    private static final String API_GATEWAY_KEY_PATH = "app/adapters/api-gateway/key.txt";
    private static final String ORDER_SERVICE_KEY_PATH = "app/adapters/order-service/key.txt";

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("gateway", KeyProvider.provideKey(API_GATEWAY_KEY_PATH));
            S2SVerification.addToTrustedStore("order-service", KeyProvider.provideKey(ORDER_SERVICE_KEY_PATH));
        };
    }

    @Bean
    OrderService orderService(OrderManagement orderManagement) {
        return new OrderService(orderManagement);
    }
}
