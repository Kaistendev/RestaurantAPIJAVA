package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.RestaurantRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantResponseDTO;
import dev.kaisten.RestaurantAPI.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDTO>> getAllRestaurants(Pageable pageable) {
        return ResponseEntity.ok(restaurantService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantRequestDTO requestDTO) {
        RestaurantResponseDTO createdRestaurant = restaurantService.create(requestDTO);
        return ResponseEntity.created(URI.create("/api/v1/restaurants/" + createdRestaurant.id())).body(createdRestaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequestDTO requestDTO) {
        return ResponseEntity.ok(restaurantService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
