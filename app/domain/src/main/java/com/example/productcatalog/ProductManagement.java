package com.example.productcatalog;

import java.util.List;

interface ProductManagement {
    List<Product> getProducts();

    Product saveProduct(Product productToSave);

    Product getProductById(Long productId);

    boolean existsByProductId(Long productId);

    Product checkProductAvailabilityAndReserve(Long productId, Integer quantity);

    void unReserveProducts(List<Product> products);
}
