package com.example.cartservice;

import com.example.cartservice.auth.JwtRequestFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
class CartManagementImpl implements CartManagement {

    private final EventRepository eventRepository;
    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final ProductMapper productMapper;
    private final EventSender eventSender;
    private final EventMapper eventMapper;
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
    public Cart getUserCart(String userId, CartStatus status) {
        return cartMapper.toDomain(cartRepository.findByUserIdAndStatus(userId, status)
                .orElseThrow(()-> new EntityNotFoundException("Cart with given user id not exists "+userId)));
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
    public Cart updateCart(Product productRequest, Cart cart) {
        Cart cartToUpdate = cartMapper.toDomain(cartRepository.findByUserIdAndStatus(cart.getUserId(), CartStatus.ACKNOWLEDGED)
                .orElseThrow(() -> new EntityNotFoundException("Cart with status ACKNOWLEDGED not exists!")));

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
    public void updateEventStatus(Long eventId, String eventStatus) {
        EventEntity eventEntity = eventRepository.findByIdAndEventStatus(eventId, EventEntity.EventStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with %d productId - not found!", eventId)));
        eventEntity.updateStatus(EventEntity.EventStatus.valueOf(eventStatus));
        eventRepository.save(eventEntity);
        log.info("Event after update status {}", eventEntity);
    }

    @Override
    @Transactional
    public Cart updateCart(Cart cart, List<EventProduct> productsToUnReserve) throws IOException, TimeoutException {
        Cart clearCart = cartMapper.toDomain(cartRepository.save(cartMapper.toEntity(cart)));
        eventUnReserveProducts(cart.getCartId(), productsToUnReserve);
        return clearCart;
    }

    @Override
    public String provideUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtRequestFilter.CustomAuthenticationToken customToken) {
            Map<String, Object> additionalParams = customToken.getAdditionalParams();
            return (String) additionalParams.get("UniqueUserId");
        }
        return null;
    }

    @Override
    @Transactional
    public Cart isCartForOrder(String userId) {
        Cart cart = cartMapper.toDomain(cartRepository.findByUserIdAndStatus(userId, CartStatus.ACKNOWLEDGED)
                .orElseThrow(() -> new EntityNotFoundException("Cart with status 'acknowledged' for order is not exists!")));
        cart.setStatus(CartStatus.PROCESS);
        cartRepository.save(cartMapper.toEntity(cart));
        return cart;
    }

    private void eventUnReserveProducts(String cartId, List<EventProduct> productsToUnReserve) throws IOException, TimeoutException {
        EventEntity eventEntity = new EventEntity(
                null, // ID encji jest generowane przez bazę danych
                cartId,
                EventEntity.EventStatus.PENDING,
                eventMapper.toEntityList(productsToUnReserve)
        );
        Long eventId = eventRepository.save(eventEntity).getId();
        eventSender.unReserveProducts(eventId, eventMapper.toRecordList(productsToUnReserve));
    }
}
