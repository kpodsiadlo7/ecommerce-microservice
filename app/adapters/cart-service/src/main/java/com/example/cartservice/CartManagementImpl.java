package com.example.cartservice;

import com.example.cartservice.feign.ProductClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class CartManagementImpl implements CartManagement {

    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final ProductMapper productMapper;
    private final CartMapper cartMapper;

    @Override
    public boolean userHasCart(String userId, final CartStatus status) {
        return cartRepository.existsByUserIdAndStatus(userId, status);
    }

    @Override
    public Cart createCartForUser(Cart cart) {
        return cartMapper.toDomain(cartRepository.save(cartMapper.toEntity(cart)));
    }

    @Override
    public Cart getUserCart(String userId) {
        return cartMapper.toDomain(cartRepository.findByUserId(userId));
    }

    @Override
    public boolean isCartIdAlreadyExists(String cartId) {
        return cartRepository.existsByCartId(cartId);
    }

    @Override
    public Product checkProductAndReserve(Long productId, Integer quantity, String authorization) {
        return productMapper.fromRecord(productClient.checkAvailabilityProduct(productId, quantity, authorization));
    }

    @Override
    @Transactional
    public void saveProcessCart(Cart cart) {
        cartRepository.save(cartMapper.toEntity(cart));
    }

    @Override
    @Transactional
    public Cart updateCart(Product productRequest, Cart cart, CartStatus status) {
        Cart cartToUpdate = cartMapper.toDomain(cartRepository.findByUserId(cart.getUserId()));
        cartToUpdate.setStatus(status);
        Product productToUpdate = cartToUpdate.getProducts().stream()
                .filter(product -> product.getProductId().equals(productRequest.getProductId()))
                .findFirst().orElse(null);
        if (productToUpdate == null) {
            cartToUpdate.addItem(productRequest);
        } else {
            //productToUpdate.updateItem(productRequest);
            cartToUpdate.updateCartItem(productRequest);
        }
        return cartMapper.toDomain(cartRepository.save(cartMapper.toEntity(cartToUpdate)));
    }

    @Override
    public void changeCartStatusToFailed(Cart cart, CartStatus cartStatus) {
        log.info("Statuts przed aktualizacją {} \n koszyk wygląda następująco {}", cart.getStatus(), cart);
        cart.setStatus(cartStatus);
        cartRepository.save(cartMapper.toEntity(cart));
    }
}
