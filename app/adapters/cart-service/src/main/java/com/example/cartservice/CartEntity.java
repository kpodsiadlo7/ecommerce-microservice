package com.example.cartservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "carts")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cartId;
    private String userId;
    private BigDecimal totalPrice;
    private CartStatus status;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;
}