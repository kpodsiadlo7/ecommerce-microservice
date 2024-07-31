package com.example.orderservice;

import com.example.orderservice.auth.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderManagementImpl implements OrderManagement {

    private final CartOrderAdapter cartOrderAdapter;
    private final OrderRepository orderRepository;
    private final EventSender eventSender;
    private final OrderMapper orderMapper;
    private final CartClient cartClient;

    @Override
    public Order fetchCartInfo(String systemToken) {
        return cartOrderAdapter.toOrder(cartClient.fetchCartInfo(systemToken));
    }

    @Override
    public Order createOrder(Order order) throws IOException, TimeoutException {
        Order savedOrder = orderMapper.toDomain(orderRepository.save(orderMapper.toEntity(order)));
        eventSender.updateCartStatus(savedOrder.getCartId(), savedOrder.getStatus());
        return savedOrder;
    }

    @Override
    public boolean isOrderIdAlreadyExists(String orderId) {
        return orderRepository.existsByOrderId(orderId);
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
    public boolean isOrderExistsForCart(String cartId) {
        return orderRepository.existsByCartId(cartId);
    }
}
