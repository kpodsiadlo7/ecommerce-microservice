package com.example.productcatalog;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductManagementImpl implements ProductManagement {

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
                productToSave.getId(),
                productToSave.getTitle(),
                productToSave.getDescription(),
                productToSave.getPrice(),
                productToSave.getQty(),
                0
        );
        productRepository.save(productEntity);
        return productMapper.toDomain(productEntity);
    }

    @Override
    public Product getProductById(Long productId) {
        return productMapper.toDomain(productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found")));
    }

    @Override
    public boolean existsByProductId(Long productId) {
        log.info("Is product exists?");
        return productRepository.existsById(productId);
    }

    @Override
    @Transactional
    public Product checkProductAvailabilityAndReserve(Long productId, Integer quantity) {
        log.info("Check product availability and reserve");
        ProductEntity availableProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found!"));
        Product product = productMapper
                .toDomain(availableProduct);
        if (product.getQty() != null && product.getQty() >= quantity) {
            log.info("Can reserve");
            updateProductFromDb(quantity, availableProduct);
            log.info("Product with productId {} successfully reserved", product.getId());
            return Product.builder()
                    .id(product.getId())
                    .price(product.getPrice())
                    .title(product.getTitle())
                    .description(product.getDescription())
                    .qty(quantity)
                    .price(product.getPrice()).build();
        }
        log.info("No enough quantity product to reserve");
        throw new IllegalArgumentException("No enough quantity product to buy");
    }

    @Override
    @Transactional
    public boolean unReserveProducts(List<Product> products) {
        products.forEach(product -> {
            ProductEntity entity = productRepository.findById(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Product %d not found!", product.getId())));
            entity.unReserveQty(product.getQty());
            productRepository.save(entity);
        });
        return true;
    }

    private void updateProductFromDb(Integer quantity, ProductEntity productEntity) {
        log.warn("Update available and reserved quantity for product with productId {}", productEntity.getId());
        productEntity.reserveQty(quantity);
        productRepository.save(productEntity);
    }
}
