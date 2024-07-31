package com.example.productcatalog;

import lombok.Builder;

@Builder
record EventStatusRecord(String eventId, Status status) {
    enum Status {
        COMPLETED,
        FAILED
    }
}
