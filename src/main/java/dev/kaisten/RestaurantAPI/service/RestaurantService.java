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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final RestaurantTableRepository tableRepository;
    private final RestaurantMapper restaurantMapper;

    @Cacheable(value = "restaurants", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<RestaurantResponseDTO> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable).map(restaurantMapper::toDto);
    }

    @Cacheable(value = "restaurants", key = "#id")
    @Transactional(readOnly = true)
    public RestaurantResponseDTO findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        return restaurantMapper.toDto(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public RestaurantResponseDTO create(RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(requestDTO);
        updateRestaurantRelations(restaurant, requestDTO);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
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

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void delete(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + id);
        }
        restaurantRepository.deleteById(id);
    }

    private void updateRestaurantRelations(Restaurant restaurant, RestaurantRequestDTO requestDTO) {
        if (requestDTO.menuIds() != null) {
            List<Menu> menusList = menuRepository.findAllById(requestDTO.menuIds());
            if (menusList.size() != requestDTO.menuIds().size()) {
                throw new ResourceNotFoundException("One or more menus not found");
            }
            restaurant.setMenus(new HashSet<>(menusList));
        }
        if (requestDTO.tableIds() != null) {
            List<RestaurantTable> tablesList = tableRepository.findAllById(requestDTO.tableIds());
            if (tablesList.size() != requestDTO.tableIds().size()) {
                throw new ResourceNotFoundException("One or more tables not found");
            }
            restaurant.setTables(new HashSet<>(tablesList));
        }
    }
}
