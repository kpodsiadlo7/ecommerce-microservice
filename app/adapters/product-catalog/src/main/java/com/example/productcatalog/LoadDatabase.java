package com.example.productcatalog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
class LoadDatabase {

    private Random random = new Random();
    private final ProductRepository productRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (productRepository.count() == 0) {
                List<Product> productList = new ArrayList<>();
                for (int i = 0; i <= 35; i++) {
                    productList.add(Product.builder()
                            .name("Product" + i)
                            .title("Title" + i)
                            .description("Description" + i)
                            .price(generateRandomPrice())
                            .qty(generateRandomQty()).build());
                }
                productRepository.saveAll(productList);
                log.info("Db: {}", productRepository.count());
            }
        };
    }

    private int generateRandomQty() {
        return 10 + (200 - 10) * random.nextInt();
    }

    private BigDecimal generateRandomPrice() {

        double randomPrice = 10 + (200 - 10) * random.nextDouble();
        return BigDecimal.valueOf(randomPrice).setScale(2, RoundingMode.HALF_UP);
    }
}
