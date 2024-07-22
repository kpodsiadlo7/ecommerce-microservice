package com.example.product_catalog;

import com.s2s.S2SVerification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductCatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogServiceApplication.class, args);

        S2SVerification.addToTrustedStore("product-catalog", "productKey");

        System.out.println(S2SVerification.verifyRequest("product-catalog",""));
    }

}
