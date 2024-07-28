package com.example.productcatalog;

import java.util.List;

interface ProductManagement {
    List<Product> getProducts();

    Product saveProduct(Product productToSave);

    Product getProductById(Long productId);
}
