package com.example.productcatalog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/all")
    ResponseEntity<List<ProductRecord>> getAllProducts() {
        return ResponseEntity.ok(productMapper.toRecordList(productService.getProducts()));
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductRecord> getProductById(@PathVariable @NotNull Long productId) {
        return ResponseEntity.ok(productMapper.toRecord(productService.getProductById(productId)));
    }

    @PostMapping
    ResponseEntity<ProductRecord> createProduct(@RequestBody @Valid ProductCreateRequest product) {
        var response = productMapper.toRecord(productService.createProduct(product));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    ResponseEntity<ProductRecord> updateProduct(@RequestBody ProductRecord productRecord) {
        var response = productMapper.toRecord(productService.updateProduct(productMapper.fromRecord(productRecord)));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
