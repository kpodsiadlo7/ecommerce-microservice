package com.example.productcatalog;

import lombok.Builder;

@Builder
public record EventStatusRecord(Long eventId, Status status) {
    enum Status {
        COMPLETED,
        FAILED
    }
}
