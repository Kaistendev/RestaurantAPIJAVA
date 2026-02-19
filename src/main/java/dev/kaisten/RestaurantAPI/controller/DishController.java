package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.service.DishService;
import dev.kaisten.RestaurantAPI.dto.DishRequestDTO;
import dev.kaisten.RestaurantAPI.dto.DishResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public ResponseEntity<Page<DishResponseDTO>> getAllDishes(Pageable pageable) {
        return ResponseEntity.ok(dishService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponseDTO> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DishResponseDTO> createDish(@Valid @RequestBody DishRequestDTO requestDTO) {
        DishResponseDTO createdDish = dishService.create(requestDTO);
        return ResponseEntity.created(URI.create("/api/v1/dishes/" + createdDish.id())).body(createdDish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponseDTO> updateDish(
            @PathVariable Long id,
            @Valid @RequestBody DishRequestDTO requestDTO) {
        return ResponseEntity.ok(dishService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
