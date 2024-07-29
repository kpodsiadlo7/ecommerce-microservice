package com.example.cartservice;

import com.example.cartservice.auth.JwtRequestFilter;
import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    JwtRequestFilter jwtRequestFilter(){
        return new JwtRequestFilter();
    }

    private static final String API_GATEWAY_KEY_PATH = "app/adapters/api-gateway/key.txt";

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("gateway", KeyProvider.provideKey(API_GATEWAY_KEY_PATH));
        };
    }
}
