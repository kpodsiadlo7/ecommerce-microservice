package com.example.cartservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "products")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    @Id
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer availableQty;
    private Integer reservedQty;
}
