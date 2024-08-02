package com.example.cartservice;

import com.s2s.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
class CartService {

    private final CartManagement cartManagement;

    Cart addProductToCart(final Long productId, Integer quantity) throws IOException {
        final String userId = cartManagement.provideUserId();
        if (userId == null) throw new IllegalArgumentException("UserId is not exists!");

        Cart cart = ensureUserHasCartAndCreate(userId);
        if (quantity == null) quantity = 1;
        preliminarilyAddProductsToCart(productId, userId, quantity, cart);
        Product product = checkProductAndReserve(productId, userId, quantity);

        if (product != null && product.getAvailableQty() != null && product.getPrice() != null) {
            return cartManagement.updateCart(product, cart);
        }
        return cartManagement.saveCart(cart);
    }

    private Product checkProductAndReserve(Long productId, String userId, Integer quantity) throws IOException {
        String systemToken = "Bearer " + JwtUtil.generateToken("cart-service", userId, "SYSTEM");
        return cartManagement.checkProductAndReserve(productId, quantity, systemToken);
    }

    private Cart ensureUserHasCartAndCreate(String userId) {
        if (!cartManagement.userHasCart(userId, CartStatus.PROCESS)) {
            Cart cart = Cart.builder()
                    .cartId(generateCartId())
                    .userId(userId)
                    .status(CartStatus.PROCESS)
                    .totalPrice(BigDecimal.ZERO)
                    .products(new ArrayList<>())
                    .build();
            return cartManagement.createCartForUser(cart);
        }
        return cartManagement.getUserCart(userId, CartStatus.PROCESS);
    }

    private void preliminarilyAddProductsToCart(Long productId, String userId, Integer quantity, Cart cart) {
        cart.getProducts().add(Product.builder().productId(productId).availableQty(quantity).build());
        cart.setUserId(userId);
    }

    Cart getMyCart() {
        final String userId = cartManagement.provideUserId();
        if (userId == null) throw new IllegalArgumentException("UserId is not exists!");

        return ensureUserHasCartAndCreate(userId);
    }

    private String generateCartId() {
        String cartId = UUID.randomUUID().toString();
        if (cartManagement.isCartIdAlreadyExists(cartId)) {
            generateCartId();
        }
        return cartId;
    }

    Cart clearMyCart() throws IOException, TimeoutException {
        Cart cart = getMyCart();
        if (cart.getProducts().isEmpty()) {
            log.info("Cart was empty");
            return cart;
        }
        log.warn("Cart was not empty - clear cart");
        return clearProductsFromCartAndUpdate(cart);
    }

    private Cart clearProductsFromCartAndUpdate(Cart cart) throws IOException, TimeoutException {
        log.info("Cart before clear products {}", cart);
        List<EventProduct> productsToUnReserve = deepCopyProducts(cart);
        cart.clearCart();
        return cartManagement.updateCart(cart, productsToUnReserve);
    }

    private List<EventProduct> deepCopyProducts(Cart cart) {
        return cart.getProducts().stream()
                .map(product -> EventProduct.builder()
                        .productId(product.getProductId())
                        .qty(product.getAvailableQty()).build()).toList();
    }

    public Cart fetchCart() {
        final String userId = cartManagement.provideUserId();
        if (userId == null) throw new IllegalArgumentException("UserId is not exists!");
        return cartManagement.fetchCartForUser(userId);
    }
}
