package com.example.orderservice;

import lombok.Builder;

@Builder
public record EventRecord(String eventId, OrderStatus status) {
}
