package com.example.productcatalog;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductManagementImpl implements ProductManagement {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<Product> getProducts() {
        return productMapper.fromEntityList(productRepository.findAll());
    }

    @Override
    @Transactional
    public Product saveProduct(Product productToSave) {
        ProductEntity productEntity = new ProductEntity(
                null,
                productToSave.getTitle(),
                productToSave.getDescription(),
                productToSave.getPrice(),
                productToSave.getQty()
        );
        productRepository.save(productEntity);
        return productMapper.toDomain(productEntity);
    }
}
