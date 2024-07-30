package com.example.cartservice;

import lombok.Builder;

@Builder
record EventProductRecord(Long productId, Integer qty) {
}
