package com.lumina.luminabackend.controller.customer;

import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.furniture.FurnitureDTO;
import com.lumina.luminabackend.service.FurnitureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/furniture")
@RequiredArgsConstructor
public class FurnitureController {

    private final FurnitureService furnitureService;

    /**
     * Get all available furniture
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FurnitureDTO>>> getAllAvailable() {
        List<FurnitureDTO> furniture = furnitureService.findAllAvailable();
        return ResponseEntity.ok(ApiResponseDTO.success("Mobiliario disponible encontrado", furniture));
    }

    /**
     * Get furniture by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FurnitureDTO>> getById(@PathVariable Integer id) {
        FurnitureDTO furniture = furnitureService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Mobiliario encontrado", furniture));
    }

    /**
     * Check availability for specific date and quantity
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponseDTO<Boolean>> checkAvailability(
            @PathVariable Integer id,
            @RequestParam Integer quantity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        boolean available = furnitureService.checkAvailability(id, quantity, date);
        String message = available ?
                "Stock disponible" : "Stock insuficiente";
        return ResponseEntity.ok(ApiResponseDTO.success(message, available));
    }
}