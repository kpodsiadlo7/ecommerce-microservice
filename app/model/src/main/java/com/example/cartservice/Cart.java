package com.example.cartservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Cart {
    private Long id;
    private String userId;
    private List<Product> products = new ArrayList<>();
    private BigDecimal totalPrice;
}
