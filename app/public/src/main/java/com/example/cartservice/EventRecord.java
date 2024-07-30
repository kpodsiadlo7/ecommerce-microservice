package com.example.cartservice;

import lombok.Builder;

import java.util.List;

@Builder
public record EventRecord(Long eventId, List<EventProductRecord> products) {
}
