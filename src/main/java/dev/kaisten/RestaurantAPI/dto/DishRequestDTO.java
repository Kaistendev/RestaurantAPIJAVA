package dev.kaisten.RestaurantAPI.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DishRequestDTO(
        @NotBlank(message = "Dish name cannot be empty")
        @Size(max = 100, message = "Dish name cannot exceed 100 characters")
        String name,

        @Size(max = 500, message = "Dish description cannot exceed 500 characters")
        String description,

        @NotNull(message = "Dish price cannot be null")
        @DecimalMin(value = "0.01", message = "Dish price must be greater than 0")
        BigDecimal price,

        Boolean active
) {}
