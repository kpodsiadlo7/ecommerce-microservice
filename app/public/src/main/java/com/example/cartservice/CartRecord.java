package com.example.cartservice;

import com.example.productcatalog.ProductRecord;

import java.math.BigDecimal;
import java.util.List;

public record CartRecord(Long id, String userId, List<ProductRecord> products, BigDecimal totalPrice) {
}
