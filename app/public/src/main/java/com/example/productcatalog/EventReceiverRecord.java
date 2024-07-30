package com.example.productcatalog;

import com.example.cartservice.EventProductRecord;

import java.util.List;

public record EventReceiverRecord(Long eventId, List<EventProductRecord> products) {
}
