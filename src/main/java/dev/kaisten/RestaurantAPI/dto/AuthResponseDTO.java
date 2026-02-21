package dev.kaisten.RestaurantAPI.dto;

import dev.kaisten.RestaurantAPI.entity.UserRole;

public record AuthResponseDTO(
        String email,
        String firstName,
        String lastName,
        UserRole role,
        String token,
        String refreshToken) {
}
