package dev.kaisten.RestaurantAPI.dto;

public record RestaurantTableResponseDTO(
        Long id,
        Integer tableNumber,
        Integer capacity,
        Boolean isAvailable,
        Long restaurantId
) {}
