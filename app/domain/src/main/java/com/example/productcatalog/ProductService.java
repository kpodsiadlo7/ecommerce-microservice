package com.example.productcatalog;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class ProductService {

    private final ProductManagement productManagement;

    List<Product> getProducts() {
        return productManagement.getProducts();
    }

    Product createProduct(ProductCreateRequest product) {
        Product productToSave = new Product(
                null,
                product.title(),
                product.description(),
                product.price(),
                product.qty()
        );
        return productManagement.saveProduct(productToSave);
    }

    Product getProductById(Long productId) {
        return productManagement.getProductById(productId);
    }

    Product updateProduct(Product product) {
        Product productFromDb = productManagement.getProductById(product.getId());
        Product productToUpdate = new Product();
        if(productFromDb != null){
            productToUpdate.setId(product.getId());
            productToUpdate.setTitle(
                    product.getTitle() == null ? productFromDb.getTitle() : product.getTitle());
            productToUpdate.setDescription(
                    product.getDescription() == null ? productFromDb.getDescription() : product.getDescription());
            productToUpdate.setPrice(
                    product.getPrice() == null ? productFromDb.getPrice() : product.getPrice());
            productToUpdate.setQty(
                    product.getQty() == null ? productFromDb.getQty() : product.getQty());
        }
        return productManagement.saveProduct(productToUpdate);
    }
}
