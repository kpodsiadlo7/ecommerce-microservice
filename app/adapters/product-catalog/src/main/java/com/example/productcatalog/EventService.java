package com.example.productcatalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final ProductManagement productManagement;

    void unReserveProducts(List<EventProductRecord> productRecords) {
        List<Product> products = productRecords.stream()
                .map(product -> Product.builder().id(product.productId()).qty(product.qty()).build())
                .toList();
        productManagement.unReserveProducts(products);
    }
}
