package com.example.cartservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
class EventService {

    private final CartManagement cartManagement;

    void updateCartStatus(EventReceiverRecord event) throws IOException, TimeoutException {
        cartManagement.updateCartStatusAfterPayment(event.eventId(), String.valueOf(event.status()));
    }

    public void updateEventStatus(EventReceiverRecord event) {
        cartManagement.updateEventStatusAfterUnsresevedProducts(event.eventId(), String.valueOf(event.status()));
    }
}
