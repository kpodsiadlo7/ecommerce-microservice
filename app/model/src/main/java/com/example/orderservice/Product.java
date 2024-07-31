package com.example.orderservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class Product {
    private Long productId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer qty;
}
