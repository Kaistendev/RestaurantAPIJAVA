package dev.kaisten.RestaurantAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MenuRequestDTO(
        @NotBlank(message = "Menu name cannot be empty")
        @Size(max = 100, message = "Menu name cannot exceed 100 characters")
        String name,

        @Size(max = 500, message = "Menu description cannot exceed 500 characters")
        String description,

        List<Long> dishIds
) {}
