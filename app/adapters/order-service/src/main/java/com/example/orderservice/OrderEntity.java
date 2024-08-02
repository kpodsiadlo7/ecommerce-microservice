package com.example.orderservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
class OrderEntity implements OrderUpdater {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderId;
    private String userId;
    private String cartId;
    private LocalDateTime orderDate;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductEntity> products;
    private BigDecimal totalPrice;
    private OrderStatus status;

    @Override
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
