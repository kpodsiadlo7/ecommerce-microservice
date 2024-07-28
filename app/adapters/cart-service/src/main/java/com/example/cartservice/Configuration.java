package com.example.cartservice;

import com.example.cartservice.auth.JwtRequestFilter;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    JwtRequestFilter jwtRequestFilter(){
        return new JwtRequestFilter();
    }
}
