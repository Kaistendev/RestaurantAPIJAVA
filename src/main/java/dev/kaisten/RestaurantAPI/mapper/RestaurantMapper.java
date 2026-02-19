package dev.kaisten.RestaurantAPI.mapper;

import dev.kaisten.RestaurantAPI.dto.RestaurantRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantResponseDTO;
import dev.kaisten.RestaurantAPI.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MenuMapper.class, RestaurantTableMapper.class})
public interface RestaurantMapper {

    RestaurantResponseDTO toDto(Restaurant restaurant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "tables", ignore = true)
    Restaurant toEntity(RestaurantRequestDTO requestDTO);
}
