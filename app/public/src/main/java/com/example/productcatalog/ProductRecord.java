package com.example.productcatalog;

import java.math.BigDecimal;

record ProductRecord(Long productId, String title, String description, BigDecimal price, Integer qty) {
}
