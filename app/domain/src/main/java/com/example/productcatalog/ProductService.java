package com.example.productcatalog;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    private final ProductManagement productManagement;

    List<Product> getProducts() {
        return productManagement.getProducts();
    }

    public Product createProduct(ProductCreateRequest product) {
        Product productToSave = new Product(
                null,
                product.title(),
                product.description(),
                product.price(),
                product.qty()
        );
        return productManagement.saveProduct(productToSave);
    }
}
