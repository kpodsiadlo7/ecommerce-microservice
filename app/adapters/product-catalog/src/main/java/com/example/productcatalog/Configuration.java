package com.example.productcatalog;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.example.productcatalog")
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    ProductService productService(ProductManagement productManagement){
        return new ProductService(productManagement);
    }

    @Bean
    LoadDatabase loadDatabase(ProductRepository productRepository){
        return new LoadDatabase(productRepository);
    }
}
