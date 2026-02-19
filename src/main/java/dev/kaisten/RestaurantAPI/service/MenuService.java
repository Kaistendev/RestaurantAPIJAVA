package dev.kaisten.RestaurantAPI.service;

import dev.kaisten.RestaurantAPI.dto.MenuRequestDTO;
import dev.kaisten.RestaurantAPI.dto.MenuResponseDTO;
import dev.kaisten.RestaurantAPI.entity.Dish;
import dev.kaisten.RestaurantAPI.entity.Menu;
import dev.kaisten.RestaurantAPI.exception.ResourceNotFoundException;
import dev.kaisten.RestaurantAPI.mapper.MenuMapper;
import dev.kaisten.RestaurantAPI.repository.DishRepository;
import dev.kaisten.RestaurantAPI.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final MenuMapper menuMapper;

    @Transactional(readOnly = true)
    public Page<MenuResponseDTO> findAll(Pageable pageable) {
        return menuRepository.findAll(pageable).map(menuMapper::toDto);
    }

    @Transactional(readOnly = true)
    public MenuResponseDTO findById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        return menuMapper.toDto(menu);
    }

    @Transactional
    public MenuResponseDTO create(MenuRequestDTO requestDTO) {
        Menu menu = menuMapper.toEntity(requestDTO);
        if (requestDTO.dishIds() != null && !requestDTO.dishIds().isEmpty()) {
            Set<Dish> dishes = new HashSet<>(dishRepository.findAllById(requestDTO.dishIds()));
            menu.setDishes(dishes);
        }
        menu = menuRepository.save(menu);
        return menuMapper.toDto(menu);
    }

    @Transactional
    public MenuResponseDTO update(Long id, MenuRequestDTO requestDTO) {
        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));

        existingMenu.setName(requestDTO.name());
        existingMenu.setDescription(requestDTO.description());

        if (requestDTO.dishIds() != null) {
            Set<Dish> dishes = new HashSet<>(dishRepository.findAllById(requestDTO.dishIds()));
            existingMenu.setDishes(dishes);
        }

        existingMenu = menuRepository.save(existingMenu);
        return menuMapper.toDto(existingMenu);
    }

    @Transactional
    public void delete(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new ResourceNotFoundException("Menu not found with id: " + id);
        }
        menuRepository.deleteById(id);
    }
}
