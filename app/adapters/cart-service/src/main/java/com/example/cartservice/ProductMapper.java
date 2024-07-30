package com.example.cartservice;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class ProductMapper {

    List<ProductRecord> toRecordList(List<Product> products) {
        List<ProductRecord> records = new ArrayList<>();
        for (var product : products) {
            records.add(toRecord(product));
        }
        return records;
    }

    private ProductRecord toRecord(Product product) {
        return new ProductRecord(
                product.getProductId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableQty()
        );
    }

    Product fromRecord(ProductRecord productRecord) {
        return Product.builder()
                .productId(productRecord.productId())
                .price(productRecord.price())
                .availableQty(productRecord.qty())
                .productId(productRecord.productId())
                .title(productRecord.title())
                .description(productRecord.description()).build();
    }

    List<ProductEntity> toEntityList(List<Product> products) {
        List<ProductEntity> entities = new ArrayList<>();
        for (Product product : products) {
            entities.add(toEntity(product));
        }
        return entities;
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getProductId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableQty(),
                product.getReservedQty()
        );
    }

    List<Product> toDomainList(List<ProductEntity> productEntities) {
        List<Product> products = new ArrayList<>();
        for (var product : productEntities) {
            products.add(fromEntity(product));
        }
        return products;
    }

    private Product fromEntity(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getAvailableQty(),
                productEntity.getReservedQty()
        );
    }
}
