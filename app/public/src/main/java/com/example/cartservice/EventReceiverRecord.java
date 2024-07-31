package com.example.cartservice;

record EventReceiverRecord(String eventId, Status status) {
    enum Status {
        COMPLETED,
        FAILED
    }
}