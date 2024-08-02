package com.example.paymentservice;

import lombok.Builder;

@Builder
record EventRecord(String eventId, PaymentStatus status) {
}