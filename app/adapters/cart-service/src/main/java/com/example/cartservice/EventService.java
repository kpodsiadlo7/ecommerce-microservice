package com.example.cartservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final CartManagement cartManagement;

    public void updateCartStatus(EventReceiverRecord event) {
        if (event == null || event.status() == null || event.eventId() == null) throw new IllegalArgumentException("Invalid event receiver record!");
        cartManagement.updateEventStatus(event.eventId(), String.valueOf(event.status()));
    }
}
