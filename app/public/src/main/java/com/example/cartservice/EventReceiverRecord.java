package com.example.cartservice;

public record EventReceiverRecord(Long eventId, Status status) {
    enum Status {
        COMPLETED,
        FAILED
    }
}