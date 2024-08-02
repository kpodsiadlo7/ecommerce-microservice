package com.example.productcatalog;

import lombok.Builder;

@Builder
record EventProductRecord(String productId, Integer qty) {
}
