package com.example.productcatalog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
class ProductEntity implements ProductUpdater {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer availableQty;
    private Integer reservedQty;

    @Override
    public void unReserveQty(Integer qty) {
        this.reservedQty -= qty;
        this.availableQty += qty;
    }

    @Override
    public void reserveQty(Integer qty) {
        this.availableQty -= qty;
        this.reservedQty += qty;
    }
}
