package com.example.productcatalog;

import lombok.Builder;

@Builder
record EventStatusRecord(Long eventId, Status status) {
    enum Status {
        COMPLETED,
        FAILED
    }
}
