package com.example.productcatalog;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductMapper {

    List<Product> fromEntityList(List<ProductEntity> productEntityList) {
        return productEntityList.stream().map(this::fromEntity).toList();
    }

    Product fromEntity(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getQty()
        );
    }

    List<ProductRecord> toRecordList(List<Product> products) {
        return products.stream().map(this::toRecord).toList();
    }

    ProductRecord toRecord(Product product){
        return new ProductRecord(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getQty()
        );
    }

    Product toDomain(ProductEntity productEntity){
        return new Product(
                productEntity.getId(),
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getQty()
        );
    }
}
