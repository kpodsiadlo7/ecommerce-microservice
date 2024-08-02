package com.example.cartservice;

import lombok.Builder;

import java.util.List;

@Builder
record EventRecord(String eventId, List<EventProductRecord> products) {
}
