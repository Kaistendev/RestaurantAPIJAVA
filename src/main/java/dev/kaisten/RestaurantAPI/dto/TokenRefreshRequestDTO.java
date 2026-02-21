package dev.kaisten.RestaurantAPI.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequestDTO(@NotBlank String refreshToken) {
}
