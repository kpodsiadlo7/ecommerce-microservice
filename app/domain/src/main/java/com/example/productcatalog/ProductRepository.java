package com.example.productcatalog;

import java.util.List;

interface ProductRepository {
    Product save(Product product);

    void saveAll(List<Product> productList);

    long count();
}
