package com.example.cartservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "products")
@Getter
@AllArgsConstructor
@NoArgsConstructor
class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer availableQty;
    private Integer reservedQty;
}
