package com.example.cartservice;

import lombok.Builder;

import java.util.List;

@Builder
record EventRecord(Long eventId, List<EventProductRecord> products) {
}
