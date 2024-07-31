package com.example.orderservice;

import java.math.BigDecimal;
import java.util.List;

public record CartInfoRecord(String cartId, List<ProductRecord> products, BigDecimal totalPrice) {
}
