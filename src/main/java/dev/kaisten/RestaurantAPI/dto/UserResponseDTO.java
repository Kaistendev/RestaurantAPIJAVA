package dev.kaisten.RestaurantAPI.dto;

import dev.kaisten.RestaurantAPI.entity.UserRole;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role
) {}
