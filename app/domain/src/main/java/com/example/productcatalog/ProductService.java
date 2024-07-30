package com.example.productcatalog;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
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
        if (productFromDb != null) {
            productToUpdate.setId(product.getId());
            productToUpdate.setTitle(
                    product.getTitle() == null ? productFromDb.getTitle() : product.getTitle());
            productToUpdate.setDescription(
                    product.getDescription() == null ? productFromDb.getDescription() : product.getDescription());
            productToUpdate.setPrice(
                    product.getPrice() == null ? productFromDb.getPrice() : product.getPrice());
            productToUpdate.setQty(
                    product.getQty() == null ? productFromDb.getQty() : product.getQty()
            );
        }
        return productManagement.saveProduct(productToUpdate);
    }

    public Product checkProductAvailability(Long productId, Integer quantity) {
        log.info("Check product availability");
        if (!productManagement.existsByProductId(productId)) {
            log.warn("Product not found");
            throw new IllegalArgumentException("Product not found!");
        }
        return productManagement.checkProductAvailabilityAndReserve(productId, quantity);
    }
}
