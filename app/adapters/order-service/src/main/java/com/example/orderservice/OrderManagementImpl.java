package com.example.orderservice;

import com.example.orderservice.auth.JwtRequestFilter;
import com.s2s.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderManagementImpl implements OrderManagement {

    private final CartOrderAdapter cartOrderAdapter;
    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final OrderMapper orderMapper;
    private final CartClient cartClient;

    @Override
    public Order fetchCartInfo(String systemToken) {
        return cartOrderAdapter.toOrder(cartClient.fetchCartInfo(systemToken));
    }

    @Override
    public Order createOrder(Order order) throws IOException {
        Order savedOrder = orderMapper.toDomain(orderRepository.save(orderMapper.toEntity(order)));
        String systemToken = "Bearer " + JwtUtil.generateToken("order-service", savedOrder.getCustomerId(), "SYSTEM");
        PaymentInfo paymentInfo = paymentClient.createPaymentForOrder(systemToken);
        if (paymentInfo == null)
            throw new RuntimeException(String.format("There was a problem with creating payment for %s", savedOrder.getOrderId()));
        updateOrderStatus(paymentInfo);
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

    @Override
    public void updateOrderStatus(PaymentInfo paymentInfo) {
        OrderEntity eventEntity = orderRepository.findByOrderId(paymentInfo.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with order id '%s' - not found!", paymentInfo.getOrderId())));
        eventEntity.updateStatus(OrderStatus.valueOf(String.valueOf(paymentInfo.getStatus())));
        orderRepository.save(eventEntity);
        log.info("Event after update status {}", eventEntity);
    }

    @Override
    public OrderInfo fetchOrderInfo(String userId) {
        OrderEntity order = orderRepository.findByUserIdAndStatus(userId, OrderStatus.PROCESS)
                .orElseThrow(() -> new EntityNotFoundException(String.format("There is no order for process payment for user %s", userId)));
        order.updateStatus(OrderStatus.PAYMENT);
        orderRepository.save(order);
        return new OrderInfo(order.getCartId(), order.getOrderId(), order.getTotalPrice());
    }
}
