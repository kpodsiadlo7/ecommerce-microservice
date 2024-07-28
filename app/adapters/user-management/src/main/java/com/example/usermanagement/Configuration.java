package com.example.usermanagement;

import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);
    private static final String PATH_KEY = "app/adapters/user-management/key.txt";

    @Bean
    UserService userService(UserManagement userManagement) {
        return new UserService(userManagement);
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            S2SVerification.addToTrustedStore("user-management", KeyProvider.provideKey(PATH_KEY));
        };
    }


}
