package com.example.productcatalog;

import java.util.List;

public interface ProductManagement {
    List<Product> getProducts();

    Product saveProduct(Product productToSave);

    Product getProductById(Long productId);
}
