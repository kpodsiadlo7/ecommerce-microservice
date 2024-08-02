package com.example.productcatalog;

import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@org.springframework.context.annotation.Configuration
public class Configuration {

    private static final String API_GATEWAY_KEY_PATH = "app/adapters/api-gateway/key.txt";
    private static final String CART_SERVICE_KEY_PATH = "app/adapters/cart-service/key.txt";
    private static final String PRODUCT_CATALOG_KEY_PATH = "app/adapters/cart-service/key.txt";

    private final EventListener eventListener;

    @Bean
    CommandLineRunner addSystemToTrustedStore() {
        return args -> {
            S2SVerification.addToTrustedStore("cart-service", KeyProvider.provideKey(CART_SERVICE_KEY_PATH));
            S2SVerification.addToTrustedStore("gateway", KeyProvider.provideKey(API_GATEWAY_KEY_PATH));
            S2SVerification.addToTrustedStore("product-catalog", KeyProvider.provideKey(PRODUCT_CATALOG_KEY_PATH));
            eventListener.listenerOnEvents();
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
