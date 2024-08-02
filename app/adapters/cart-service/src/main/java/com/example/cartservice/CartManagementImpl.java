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
                .orElseThrow(() -> new EntityNotFoundException("Cart with given user id not exists " + userId)));
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
        Cart cartToUpdate = cartMapper.toDomain(cartRepository.findByUserIdAndStatus(cart.getUserId(), CartStatus.PROCESS)
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
    public Cart fetchCartForUser(String userId) {
        Cart cart = cartMapper.toDomain(cartRepository.findByUserIdAndStatus(userId, CartStatus.PROCESS)
                .orElseThrow(() -> new EntityNotFoundException("Cart with status 'acknowledged' for order is not exists!")));
        cart.setStatus(CartStatus.CLOSED);
        cartRepository.save(cartMapper.toEntity(cart));
        return cart;
    }

    @Override
    public void updateEventStatusAfterUnsresevedProducts(String eventId, String eventStatus) {
        EventEntity event = eventRepository.findByCartId(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with given cart id '%s' not exists!", eventId)));
        event.updateStatus(EventEntity.EventStatus.valueOf(eventStatus));
        eventRepository.save(event);
        log.info("Event '{}' updated -> status '{}'", eventId, eventStatus);
    }

    @Override
    public void updateCartStatusAfterPayment(String eventId, String eventStatus) throws IOException, TimeoutException {
        if (EventEntity.EventStatus.valueOf(eventStatus).equals(EventEntity.EventStatus.FAILED)) {
            Cart cart = cartMapper.toDomain(cartRepository.findByCartIdAndStatus(eventId, CartStatus.CLOSED)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Cart with given id '%s' not exists", eventId))));
            List<EventProduct> products = eventMapper.toEventProductList(cart.getProducts());
            createEvent(eventId, products);
            eventSender.unReserveProducts(eventId, eventMapper.toEventProductRecordList(products));
            log.warn("Event order failed - unreserved products {}", products);
        }
        log.info("Event '{}' status '{}'", eventId, eventStatus);
    }

    private void eventUnReserveProducts(String cartId, List<EventProduct> productsToUnReserve) throws IOException, TimeoutException {
        EventEntity eventEntity = createEvent(cartId, productsToUnReserve);
        Long eventId = eventEntity.getId();
        eventSender.unReserveProducts(String.valueOf(eventId), eventMapper.toEventProductRecordList(productsToUnReserve));
    }

    private EventEntity createEvent(String cartId, List<EventProduct> productsToUnReserve) {
        EventEntity eventEntity = new EventEntity(
                null, // ID encji jest generowane przez bazę danych
                cartId,
                EventEntity.EventStatus.PROCESS,
                eventMapper.toEntityList(productsToUnReserve)
        );
        return eventRepository.save(eventEntity);
    }
}
