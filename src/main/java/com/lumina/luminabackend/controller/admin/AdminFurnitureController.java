package com.lumina.luminabackend.controller.admin;


import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.furniture.AdminFurnitureDTO;
import com.lumina.luminabackend.dto.furniture.CreateFurnitureDTO;
import com.lumina.luminabackend.dto.furniture.UpdateFurnitureDTO;
import com.lumina.luminabackend.service.FurnitureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/furniture")
@RequiredArgsConstructor
public class AdminFurnitureController {

    private final FurnitureService furnitureService;

    /**
     * Get all furniture including out of stock
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AdminFurnitureDTO>>> getAllForAdmin() {
        List<AdminFurnitureDTO> furniture = furnitureService.findAllForAdmin();
        return ResponseEntity.ok(ApiResponseDTO.success("Lista de mobiliario obtenida", furniture));
    }

    /**
     * Get furniture by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminFurnitureDTO>> getByIdForAdmin(@PathVariable Integer id) {
        AdminFurnitureDTO furniture = furnitureService.findByIdForAdmin(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Mobiliario encontrado", furniture));
    }

    /**
     * Search furniture by name
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<AdminFurnitureDTO>>> searchByName(
            @RequestParam String name) {
        List<AdminFurnitureDTO> furniture = furnitureService.searchByName(name);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", furniture));
    }

    /**
     * Create new furniture
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AdminFurnitureDTO>> createFurniture(
            @Valid @RequestBody CreateFurnitureDTO createDTO) {
        AdminFurnitureDTO created = furnitureService.createFurniture(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Mobiliario creado exitosamente", created));
    }

    /**
     * Update existing furniture
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminFurnitureDTO>> updateFurniture(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateFurnitureDTO updateDTO) {
        AdminFurnitureDTO updated = furnitureService.updateFurniture(id, updateDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Mobiliario actualizado exitosamente", updated));
    }

    /**
     * Delete furniture
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteFurniture(@PathVariable Integer id) {
        furnitureService.deleteFurniture(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Mobiliario eliminado exitosamente", null));
    }

}