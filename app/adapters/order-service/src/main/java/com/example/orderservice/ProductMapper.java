package com.example.orderservice;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class ProductMapper {

    List<Product> toDomainList(List<ProductRecord> productRecords) {
        return productRecords.stream().map(this::toDomain).toList();
    }

    Product toDomain(ProductRecord productRecord) {
        return new Product(
                productRecord.productId(),
                productRecord.title(),
                productRecord.description(),
                productRecord.price(),
                productRecord.qty()
        );
    }

    List<ProductRecord> toRecordList(List<Product> products) {
        return products.stream().map(this::toRecord).toList();
    }

    ProductRecord toRecord(Product product) {
        return new ProductRecord(
                product.getProductId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getQty()
        );
    }

    public List<ProductEntity> toEntityList(List<Product> products) {
        List<ProductEntity> productEntities = new ArrayList<>();
        for (var product : products) {
            productEntities.add(toEntity(product));
        }
        return productEntities;
    }

    ProductEntity toEntity(Product product) {
        return new ProductEntity(
                null, // ID encji jest generowane przez bazę danych
                product.getProductId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getQty()
        );
    }

    List<Product> fromEntityList(List<ProductEntity> products) {
        return products.stream().map(this::fromEntity).toList();
    }

    Product fromEntity(ProductEntity productEntity) {
        return new Product(
                productEntity.getProductId(),
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getQty()
        );
    }
}
