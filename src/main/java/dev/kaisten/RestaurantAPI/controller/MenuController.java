package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.MenuRequestDTO;
import dev.kaisten.RestaurantAPI.dto.MenuResponseDTO;
import dev.kaisten.RestaurantAPI.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<Page<MenuResponseDTO>> getAllMenus(Pageable pageable) {
        return ResponseEntity.ok(menuService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MenuResponseDTO> createMenu(@Valid @RequestBody MenuRequestDTO requestDTO) {
        MenuResponseDTO createdMenu = menuService.create(requestDTO);
        return ResponseEntity.created(URI.create("/api/v1/menus/" + createdMenu.id())).body(createdMenu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> updateMenu(
            @PathVariable Long id,
            @Valid @RequestBody MenuRequestDTO requestDTO) {
        return ResponseEntity.ok(menuService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
