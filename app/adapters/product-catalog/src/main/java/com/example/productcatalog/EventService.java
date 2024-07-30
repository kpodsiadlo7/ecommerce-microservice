package com.example.productcatalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final ProductManagement productManagement;

    boolean unReserveProducts(EventReceiverRecord eventReceiverRecord) {
        if (eventReceiverRecord == null || eventReceiverRecord.products() == null) return false;
        List<Product> products = eventReceiverRecord.products().stream()
                .map(product -> Product.builder().id(product.productId()).qty(product.qty()).build())
                .toList();
        return productManagement.unReserveProducts(products);
    }
}
