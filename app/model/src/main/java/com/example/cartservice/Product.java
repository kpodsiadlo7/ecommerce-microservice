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
class Product {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer qty;
}
