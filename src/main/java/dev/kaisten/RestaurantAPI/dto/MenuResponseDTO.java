package dev.kaisten.RestaurantAPI.dto;

import java.util.List;

public record MenuResponseDTO(
        Long id,
        String name,
        String description,
        List<DishResponseDTO> dishes
) {}
