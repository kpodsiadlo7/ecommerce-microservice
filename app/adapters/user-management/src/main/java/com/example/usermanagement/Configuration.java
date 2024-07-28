package com.example.usermanagement;

import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private static final String GATEWAY_SECRET_KEY_PATH = "app/adapters/api-gateway/key.txt";
    private static final String USER_MANAGEMENT_SECRET_KEY_PATH = "app/adapters/user-management/key.txt";

    @Bean
    UserService userService(UserManagement userManagement) {
        return new UserService(userManagement);
    }

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("gateway", KeyProvider.provideKey(GATEWAY_SECRET_KEY_PATH));
            S2SVerification.addToTrustedStore("user-management", KeyProvider.provideKey(USER_MANAGEMENT_SECRET_KEY_PATH));
        };
    }
}
