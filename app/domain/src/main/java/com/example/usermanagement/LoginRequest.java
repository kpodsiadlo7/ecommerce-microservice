package com.example.usermanagement;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public record LoginRequest(@NotEmpty(message = "Login cannot be null")
                           @Size(max = 16, message = "Login must be maximum 16 characters long")
                           String login,

                           @NotEmpty(message = "Password cannot be null")
                           @Size(max = 16, message = "Password must be maximum 16 characters long")
                           String password) {
}
