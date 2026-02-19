package dev.kaisten.RestaurantAPI.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RestaurantTableRequestDTO(
        @NotNull(message = "Table number cannot be null")
        Integer tableNumber,

        @NotNull(message = "Capacity cannot be null")
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,

        Boolean isAvailable,

        Long restaurantId
) {}
