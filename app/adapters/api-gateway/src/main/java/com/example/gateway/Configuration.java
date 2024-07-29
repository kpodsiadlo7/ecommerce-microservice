package com.example.gateway;

import com.example.apigateway.UserManagementClient;
import com.example.gateway.auth.CustomGlobalFilter;
import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@org.springframework.context.annotation.Configuration
public class Configuration {

    private final UserManagementClient userManagementClient;

    private static final String USER_MANAGEMENT_KEY_PATH = "app/adapters/user-management/key.txt";

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

    @Bean
    public CustomGlobalFilter customGlobalFilter() {
        return new CustomGlobalFilter(userManagementClient);
    }

    @Bean
    HttpMessageConverters HttpMessageConverters(){
        return new HttpMessageConverters();
    }
}
