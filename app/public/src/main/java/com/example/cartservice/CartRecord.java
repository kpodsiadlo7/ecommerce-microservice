package com.example.cartservice;

import java.math.BigDecimal;
import java.util.List;

record CartRecord(String cartId, String userId, List<ProductRecord> products, BigDecimal totalPrice) {
}
