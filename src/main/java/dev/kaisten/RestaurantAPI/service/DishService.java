package dev.kaisten.RestaurantAPI.service;

import dev.kaisten.RestaurantAPI.mapper.DishMapper;
import dev.kaisten.RestaurantAPI.dto.DishRequestDTO;
import dev.kaisten.RestaurantAPI.dto.DishResponseDTO;
import dev.kaisten.RestaurantAPI.entity.Dish;
import dev.kaisten.RestaurantAPI.repository.DishRepository;
import dev.kaisten.RestaurantAPI.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    @Cacheable(value = "dishes", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<DishResponseDTO> findAll(Pageable pageable) {
        return dishRepository.findAll(pageable)
                .map(dishMapper::toDto);
    }

    @Cacheable(value = "dishes", key = "#id")
    @Transactional(readOnly = true)
    public DishResponseDTO findById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + id));
        return dishMapper.toDto(dish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public DishResponseDTO create(DishRequestDTO requestDTO) {
        Dish dish = dishMapper.toEntity(requestDTO);
        dish = dishRepository.save(dish);
        return dishMapper.toDto(dish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public DishResponseDTO update(Long id, DishRequestDTO requestDTO) {
        Dish existingDish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + id));

        dishMapper.updateEntityFromDto(requestDTO, existingDish);
        existingDish = dishRepository.save(existingDish);
        return dishMapper.toDto(existingDish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found with id: " + id);
        }
        dishRepository.deleteById(id);
    }
}
