package dev.kaisten.RestaurantAPI.mapper;

import dev.kaisten.RestaurantAPI.dto.UserRequestDTO;
import dev.kaisten.RestaurantAPI.dto.UserResponseDTO;
import dev.kaisten.RestaurantAPI.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO requestDTO);
}
