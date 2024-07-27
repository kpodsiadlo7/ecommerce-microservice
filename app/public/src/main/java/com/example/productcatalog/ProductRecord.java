package com.example.productcatalog;

import java.math.BigDecimal;

public record ProductRecord(Long id, String title, String description, BigDecimal price, int qty) {
}
