package com.example.orderservice;

import java.math.BigDecimal;

public record ProductRecord(Long productId, String title, String description, BigDecimal price, Integer qty) {
}
