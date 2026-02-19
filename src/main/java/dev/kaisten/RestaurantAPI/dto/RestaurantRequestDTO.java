package dev.kaisten.RestaurantAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RestaurantRequestDTO(
        @NotBlank(message = "Restaurant name cannot be empty")
        @Size(max = 100, message = "Restaurant name cannot exceed 100 characters")
        String name,

        @Size(max = 255, message = "Restaurant address cannot exceed 255 characters")
        String address,

        List<Long> menuIds,
        
        List<Long> tableIds
) {}
