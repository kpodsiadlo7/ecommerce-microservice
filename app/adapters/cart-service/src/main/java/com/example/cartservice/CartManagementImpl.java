package com.example.cartservice;

import com.example.cartservice.feign.ProductClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
class CartManagementImpl implements CartManagement {

    private final EventProductMapper eventProductMapper;
    private final EventRepository eventRepository;
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
    public Cart saveCart(Cart cart) {
        return cartMapper.toDomain(cartRepository.save(cartMapper.toEntity(cart)));
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
            cartToUpdate.updateCartItem(productRequest);
        }
        return cartMapper.toDomain(cartRepository.save(cartMapper.toEntity(cartToUpdate)));
    }

    @Override
    public void changeCartStatusToFailed(Cart cart, CartStatus cartStatus) {
        log.info("Status przed aktualizacją {} \n koszyk wygląda następująco {}", cart.getStatus(), cart);
        cart.setStatus(cartStatus);
        cartRepository.save(cartMapper.toEntity(cart));
    }

    @Override
    @Transactional
    public Cart updateCart(Cart cart, List<EventProduct> productsToUnReserve) throws IOException, TimeoutException {
        Cart clearCart = cartMapper.toDomain(cartRepository.save(cartMapper.toEntity(cart)));
        eventUnReserveProducts(productsToUnReserve);
        return clearCart;
    }

    private void eventUnReserveProducts(List<EventProduct> productsToUnReserve) throws IOException, TimeoutException {
        Event event = new Event(
                null, // ID encji jest generowane przez bazę danych
                EventStatus.PENDING,
                eventProductMapper.toEntityList(productsToUnReserve)
        );
        eventRepository.save(event);
        event.unReserveProducts(eventProductMapper.toRecordList(productsToUnReserve));
    }
}
