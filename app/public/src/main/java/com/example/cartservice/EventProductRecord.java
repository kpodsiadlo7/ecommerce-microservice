package com.example.cartservice;

import lombok.Builder;

@Builder
public record EventProductRecord(Long productId, Integer qty) {
}
