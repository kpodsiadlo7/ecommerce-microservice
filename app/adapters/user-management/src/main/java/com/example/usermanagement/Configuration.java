package com.example.usermanagement;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    UserService userService(UserManagement userManagement){
        return new UserService(userManagement);
    }
}
