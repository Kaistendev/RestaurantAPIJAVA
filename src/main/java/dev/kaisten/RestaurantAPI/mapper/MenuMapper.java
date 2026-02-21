package dev.kaisten.RestaurantAPI.mapper;

import dev.kaisten.RestaurantAPI.dto.MenuRequestDTO;
import dev.kaisten.RestaurantAPI.dto.MenuResponseDTO;
import dev.kaisten.RestaurantAPI.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = DishMapper.class)
public interface MenuMapper {

    MenuResponseDTO toDto(Menu menu);

    List<MenuResponseDTO> toDtoList(List<Menu> menus);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Menu toEntity(MenuRequestDTO requestDTO);
}
