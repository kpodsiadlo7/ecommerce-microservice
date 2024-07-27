package com.example.productcatalog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    ResponseEntity<List<ProductRecord>> getAllProducts() {
        return ResponseEntity.ok(productMapper.toRecordList(productService.getProducts()));
    }

    @PostMapping("/product")
    ResponseEntity<ProductRecord> createProduct(@RequestBody @Valid ProductCreateRequest product) {
        var response = productMapper.toRecord(productService.createProduct(product));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
