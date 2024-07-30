package com.example.productcatalog;

import java.math.BigDecimal;

public record ProductRecord(Long productId, String title, String description, BigDecimal price, Integer qty) {
}
