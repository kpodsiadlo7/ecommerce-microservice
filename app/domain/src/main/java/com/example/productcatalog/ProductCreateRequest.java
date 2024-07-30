package com.example.productcatalog;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(@NotEmpty(message = "Product title cannot be empty")
                                   String title,

                                   @NotEmpty(message = "Product description cannot be empty")
                                   String description,

                                   @NotNull(message = "Product price cannot be empty")
                                   @DecimalMin(value = "0.0", inclusive = false, message = "Price must be a positive number with up to two decimal places")
                                   BigDecimal price,

                                   @NotNull(message = "Product quantity cannot be empty")
                                   @Min(value = 0, message = "Must be 0 or a positive integer")
                                   Integer qty) {
}
