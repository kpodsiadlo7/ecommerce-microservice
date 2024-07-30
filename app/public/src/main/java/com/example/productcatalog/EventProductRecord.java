package com.example.productcatalog;

import lombok.Builder;

@Builder
record EventProductRecord(Long productId, Integer qty) {
}
