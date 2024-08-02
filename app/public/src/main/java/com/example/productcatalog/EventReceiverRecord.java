package com.example.productcatalog;

import java.util.List;

record EventReceiverRecord(String eventId, List<EventProductRecord> products) {
}
