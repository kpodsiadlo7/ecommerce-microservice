package com.example.orderservice;

import java.math.BigDecimal;
import java.util.List;

public record CartInfoRecord(List<ProductRecord> products, BigDecimal totalPrice) {
}
