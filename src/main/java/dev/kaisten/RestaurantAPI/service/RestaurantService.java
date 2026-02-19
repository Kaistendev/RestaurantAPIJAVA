package dev.kaisten.RestaurantAPI.service;

import dev.kaisten.RestaurantAPI.dto.RestaurantRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantResponseDTO;
import dev.kaisten.RestaurantAPI.entity.Menu;
import dev.kaisten.RestaurantAPI.entity.Restaurant;
import dev.kaisten.RestaurantAPI.entity.RestaurantTable;
import dev.kaisten.RestaurantAPI.exception.ResourceNotFoundException;
import dev.kaisten.RestaurantAPI.mapper.RestaurantMapper;
import dev.kaisten.RestaurantAPI.repository.MenuRepository;
import dev.kaisten.RestaurantAPI.repository.RestaurantRepository;
import dev.kaisten.RestaurantAPI.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final RestaurantTableRepository tableRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional(readOnly = true)
    public Page<RestaurantResponseDTO> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable).map(restaurantMapper::toDto);
    }

    @Transactional(readOnly = true)
    public RestaurantResponseDTO findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional
    public RestaurantResponseDTO create(RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(requestDTO);
        updateRestaurantRelations(restaurant, requestDTO);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional
    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        existingRestaurant.setName(requestDTO.name());
        existingRestaurant.setAddress(requestDTO.address());
        updateRestaurantRelations(existingRestaurant, requestDTO);

        existingRestaurant = restaurantRepository.save(existingRestaurant);
        return restaurantMapper.toDto(existingRestaurant);
    }

    @Transactional
    public void delete(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + id);
        }
        restaurantRepository.deleteById(id);
    }

    private void updateRestaurantRelations(Restaurant restaurant, RestaurantRequestDTO requestDTO) {
        if (requestDTO.menuIds() != null) {
            Set<Menu> menus = new HashSet<>(menuRepository.findAllById(requestDTO.menuIds()));
            restaurant.setMenus(menus);
        }
        if (requestDTO.tableIds() != null) {
            Set<RestaurantTable> tables = new HashSet<>(tableRepository.findAllById(requestDTO.tableIds()));
            restaurant.setTables(tables);
        }
    }
}
