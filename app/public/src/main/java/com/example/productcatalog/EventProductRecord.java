package com.example.productcatalog;

import lombok.Builder;

@Builder
public record EventProductRecord(Long productId, Integer qty) {
}
