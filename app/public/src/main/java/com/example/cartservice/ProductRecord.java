package com.example.cartservice;

import java.math.BigDecimal;

record ProductRecord(Long productId, String title, String description, BigDecimal price, Integer qty) {
}
