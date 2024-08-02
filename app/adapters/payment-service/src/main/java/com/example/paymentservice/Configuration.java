package com.example.paymentservice;

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

    //TODO wynieśc wszystkie takie path do jednego miejsca
    private static final String ORDER_SERVICE_KEY_PATH = "app/adapters/order-service/key.txt";
    private static final String PAYMENT_SERVICE_KEY_PATH = "app/adapters/payment-service/key.txt";

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("order-service", KeyProvider.provideKey(ORDER_SERVICE_KEY_PATH));
            S2SVerification.addToTrustedStore("payment-service", KeyProvider.provideKey(PAYMENT_SERVICE_KEY_PATH));
        };
    }

    @Bean
    PaymentService paymentService(PaymentManager paymentManager){
        return new PaymentService(paymentManager);
    }
}
