package com.example.paymentservice;

import com.example.paymentservice.auth.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
class PaymentManagerImpl implements PaymentManager {

    private final OrderClient orderClient;
    private final OrderMapper orderMapper;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final EventSender eventSender;

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
    public OrderInfo fetchOrderInfo(String systemToken) {
        return orderMapper.fromRecord(orderClient.fetchCartInfo(systemToken));
    }

    @Override
    public boolean isPaymentIdAlreadyExists(String paymentId) {
        return paymentRepository.existsByPaymentId(paymentId);
    }

    @Override
    public Payment createPayment(Payment newPayment) {
        return paymentMapper.toDomain(paymentRepository.save(paymentMapper.toEntity(newPayment)));
    }

    @Override
    public void eventPaymentStatus(Payment payment) throws IOException, TimeoutException {
        eventSender.updatePaymentStatus(payment.getCartId(), payment.getStatus());
    }
}
