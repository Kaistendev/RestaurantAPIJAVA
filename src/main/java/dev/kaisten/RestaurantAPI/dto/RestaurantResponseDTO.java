package dev.kaisten.RestaurantAPI.dto;

import java.util.List;

public record RestaurantResponseDTO(
        Long id,
        String name,
        String address,
        List<MenuResponseDTO> menus,
        List<RestaurantTableResponseDTO> tables
) {}
