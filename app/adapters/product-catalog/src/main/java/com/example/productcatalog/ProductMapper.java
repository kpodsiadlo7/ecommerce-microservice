package com.example.productcatalog;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductMapper {

    List<Product> fromEntityList(List<ProductEntity> productEntities) {
        List<Product> products = new ArrayList<>();
        for (var product : productEntities) {
            products.add(toDomain(product));
        }
        return products;
    }

    Product fromEntity(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getAvailableQty()
        );
    }

    List<ProductRecord> toRecordList(List<Product> products) {
        List<ProductRecord> records = new ArrayList<>();
        for (var product : products) {
            records.add(toRecord(product));
        }
        return records;
    }

    ProductRecord toRecord(Product product) {
        return new ProductRecord(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getQty()
        );
    }

    Product toDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getAvailableQty()
        );
    }

    Product fromRecord(ProductRecord productRecord) {
        return new Product(
                productRecord.productId(),
                productRecord.title(),
                productRecord.description(),
                productRecord.price(),
                productRecord.qty()
        );
    }
}
