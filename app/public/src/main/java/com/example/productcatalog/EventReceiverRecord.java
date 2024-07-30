package com.example.productcatalog;

import java.util.List;

record EventReceiverRecord(Long eventId, List<EventProductRecord> products) {
}
