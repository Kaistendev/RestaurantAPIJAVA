package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.RestaurantTableRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantTableResponseDTO;
import dev.kaisten.RestaurantAPI.service.RestaurantTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    @GetMapping
    public ResponseEntity<Page<RestaurantTableResponseDTO>> getAllTables(Pageable pageable) {
        return ResponseEntity.ok(tableService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTableResponseDTO> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RestaurantTableResponseDTO> createTable(@Valid @RequestBody RestaurantTableRequestDTO requestDTO) {
        RestaurantTableResponseDTO createdTable = tableService.create(requestDTO);
        return ResponseEntity.created(URI.create("/api/v1/tables/" + createdTable.id())).body(createdTable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTableResponseDTO> updateTable(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantTableRequestDTO requestDTO) {
        return ResponseEntity.ok(tableService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
