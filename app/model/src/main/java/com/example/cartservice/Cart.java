package com.example.cartservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Cart {
    private Long id;
    private String cartId;
    private String userId;
    private BigDecimal totalPrice;
    private CartStatus status;
    private List<Product> products;

    void addItem(Product item) {
        products.add(item);
        calculateTotal();
    }

    void removeItem(Long productId) {
        products.removeIf(item -> item.getProductId().equals(productId));
        calculateTotal();
    }

    private void calculateTotal() {
        totalPrice = products.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getAvailableQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    void updateCartItem(Product productRequest) {
        products.stream()
                .filter(e -> e.getProductId().equals(productRequest.getProductId()))
                .findFirst()
                .ifPresent(e -> e.setAvailableQty(e.getAvailableQty() + productRequest.getAvailableQty()));
        calculateTotal();
    }

    void clearCart(){
        this.products.clear();
        calculateTotal();
    }
}
