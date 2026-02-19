package dev.kaisten.RestaurantAPI.service;

import dev.kaisten.RestaurantAPI.dto.RestaurantTableRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantTableResponseDTO;
import dev.kaisten.RestaurantAPI.entity.Restaurant;
import dev.kaisten.RestaurantAPI.entity.RestaurantTable;
import dev.kaisten.RestaurantAPI.exception.ResourceNotFoundException;
import dev.kaisten.RestaurantAPI.mapper.RestaurantTableMapper;
import dev.kaisten.RestaurantAPI.repository.RestaurantRepository;
import dev.kaisten.RestaurantAPI.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantTableService {

    private final RestaurantTableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableMapper tableMapper;

    @Transactional(readOnly = true)
    public Page<RestaurantTableResponseDTO> findAll(Pageable pageable) {
        return tableRepository.findAll(pageable).map(tableMapper::toDto);
    }

    @Transactional(readOnly = true)
    public RestaurantTableResponseDTO findById(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        return tableMapper.toDto(table);
    }

    @Transactional
    public RestaurantTableResponseDTO create(RestaurantTableRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findById(requestDTO.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + requestDTO.restaurantId()));
        
        RestaurantTable table = tableMapper.toEntity(requestDTO, restaurant);
        table = tableRepository.save(table);
        return tableMapper.toDto(table);
    }

    @Transactional
    public RestaurantTableResponseDTO update(Long id, RestaurantTableRequestDTO requestDTO) {
        RestaurantTable existingTable = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));

        existingTable.setTableNumber(requestDTO.tableNumber());
        existingTable.setCapacity(requestDTO.capacity());
        
        if (requestDTO.isAvailable() != null) {
            existingTable.setIsAvailable(requestDTO.isAvailable());
        }

        if (requestDTO.restaurantId() != null && !requestDTO.restaurantId().equals(existingTable.getRestaurant().getId())) {
            Restaurant restaurant = restaurantRepository.findById(requestDTO.restaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + requestDTO.restaurantId()));
            existingTable.setRestaurant(restaurant);
        }

        existingTable = tableRepository.save(existingTable);
        return tableMapper.toDto(existingTable);
    }

    @Transactional
    public void delete(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table not found with id: " + id);
        }
        tableRepository.deleteById(id);
    }
}
