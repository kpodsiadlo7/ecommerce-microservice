package com.example.productcatalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final ProductManagement productManagement;

    boolean unreserveProducts(EventReceiverRecord eventReceiverRecord) {
        if (eventReceiverRecord == null || eventReceiverRecord.products() == null) return false;
        List<Product> products = eventReceiverRecord.products().stream()
                .map(product -> Product.builder().id(Long.valueOf(product.productId())).qty(product.qty()).build())
                .toList();
        return productManagement.unreserveProducts(products);
    }
}
