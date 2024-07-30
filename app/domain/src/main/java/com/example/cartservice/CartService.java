package com.example.cartservice;

import com.s2s.JwtUtil;
import com.s2s.S2SVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class CartService {

    private final CartManagement cartManagement;

    public Cart addProductToCart(final Long productId, final String userId, Integer quantity) throws IOException {
        Cart cart = ensureUserHasCartAndCreate(userId, CartStatus.ACKNOWLEDGED);
        if (quantity == null) quantity = 1;
        preliminarilyAddProductsToCart(productId, userId, quantity, cart);
        Product product = checkProductAndReserve(productId, userId, quantity);

        if (product != null && product.getAvailableQty() != null && product.getPrice() != null) {
            return cartManagement.updateCart(product, cart, CartStatus.ACKNOWLEDGED);
        }
        return cartManagement.saveCart(cart);
    }

    private Product checkProductAndReserve(Long productId, String userId, Integer quantity) throws IOException {
        String systemToken = generateToken(userId);
        return cartManagement.checkProductAndReserve(productId, quantity, systemToken);
    }

    private static String generateToken(String userId) throws IOException {
        String systemName = "cart-service";
        return "Bearer " + JwtUtil.generateToken(systemName, userId, S2SVerification.getSecretSystemKey(systemName), "SYSTEM");
    }

    private Cart ensureUserHasCartAndCreate(String userId, CartStatus status) {
        if (!cartManagement.userHasCart(userId, status)) {
            Cart cart = Cart.builder()
                    .cartId(generateCartId())
                    .userId(userId)
                    .status(CartStatus.ACKNOWLEDGED)
                    .totalPrice(BigDecimal.ZERO)
                    .products(new ArrayList<>())
                    .build();
            return cartManagement.createCartForUser(cart);
        }
        return cartManagement.getUserCart(userId);
    }

    private void preliminarilyAddProductsToCart(Long productId, String userId, Integer quantity, Cart cart) {
        cart.getProducts().add(Product.builder().productId(productId).availableQty(quantity).build());
        cart.setUserId(userId);
    }

    public Cart getMyCart(final String userId) {
        return ensureUserHasCartAndCreate(userId, CartStatus.ACKNOWLEDGED);
    }

    private String generateCartId() {
        String cartId = UUID.randomUUID().toString();
        if (cartManagement.isCartIdAlreadyExists(cartId)) {
            generateCartId();
        }
        return cartId;
    }

    public Cart clearMyCart(String userId) {
        Cart cart = getMyCart(userId);
        if (cart.getProducts().isEmpty()) {
            log.info("Cart was empty");
            return cart;
        }
        log.warn("Cart was not empty - clear cart");
        return ClearProductsFromCartAndUpdate(cart);
    }

    private Cart ClearProductsFromCartAndUpdate(Cart cart) {
        log.info("Cart before clear products {}", cart);
        cart.clearCart();
        log.warn("Cart after products cleared {} ", cart);
        return cartManagement.updateCart(cart);
    }
}
