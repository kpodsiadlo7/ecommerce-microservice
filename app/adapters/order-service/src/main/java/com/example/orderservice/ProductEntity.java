package com.example.orderservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    @Id
    private Long productId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer qty;
}
