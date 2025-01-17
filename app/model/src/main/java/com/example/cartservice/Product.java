package com.example.cartservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long productId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer availableQty;
    private Integer reservedQty;

    public void updateItem(Product productRequest) {
        this.availableQty += productRequest.getAvailableQty();
    }
}
