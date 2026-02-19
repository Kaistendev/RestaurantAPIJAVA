package dev.kaisten.RestaurantAPI.dto;

import java.math.BigDecimal;

public record DishResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean active
) {}
