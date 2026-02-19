package dev.kaisten.RestaurantAPI.mapper;

import dev.kaisten.RestaurantAPI.dto.RestaurantTableRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantTableResponseDTO;
import dev.kaisten.RestaurantAPI.entity.Restaurant;
import dev.kaisten.RestaurantAPI.entity.RestaurantTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantTableMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    RestaurantTableResponseDTO toDto(RestaurantTable table);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", source = "restaurant")
    RestaurantTable toEntity(RestaurantTableRequestDTO requestDTO, Restaurant restaurant);
}
