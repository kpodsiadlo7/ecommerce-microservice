package com.example.productcatalog;

import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "com.example.productcatalog")
@org.springframework.context.annotation.Configuration
public class Configuration {

    private static final String API_GATEWAY_KEY_PATH = "app/adapters/api-gateway/key.txt";
    private static final String CART_SERVICE_KEY_PATH = "app/adapters/cart-service/key.txt";

    private final EventManager eventManager;

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("cart-service", KeyProvider.provideKey(CART_SERVICE_KEY_PATH));
            S2SVerification.addToTrustedStore("gateway", KeyProvider.provideKey(API_GATEWAY_KEY_PATH));
            eventManager.listenerOnEvents();
        };
    }

    @Bean
    ProductService productService(ProductManagement productManagement) {
        return new ProductService(productManagement);
    }

    @Bean
    LoadDatabase loadDatabase(ProductRepository productRepository) {
        return new LoadDatabase(productRepository);
    }
}
