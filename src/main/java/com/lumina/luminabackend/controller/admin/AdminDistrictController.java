package com.lumina.luminabackend.controller.admin;

import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.district.AdminDistrictDTO;
import com.lumina.luminabackend.dto.district.CreateDistrictDTO;
import com.lumina.luminabackend.dto.district.UpdateDistrictDTO;
import com.lumina.luminabackend.service.DistrictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/districts")
@RequiredArgsConstructor
public class AdminDistrictController {

    private final DistrictService districtService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AdminDistrictDTO>>> getAllForAdmin() {
        List<AdminDistrictDTO> districts = districtService.findAllForAdmin();
        return ResponseEntity.ok(ApiResponseDTO.success("Lista de distritos obtenida", districts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminDistrictDTO>> getByIdForAdmin(@PathVariable Integer id) {
        AdminDistrictDTO district = districtService.findByIdForAdmin(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Distrito encontrado", district));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<AdminDistrictDTO>>> searchByName(@RequestParam String name) {
        List<AdminDistrictDTO> districts = districtService.searchByName(name);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", districts));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<AdminDistrictDTO>> createDistrict(
            @Valid @RequestBody CreateDistrictDTO createDTO) {
        AdminDistrictDTO created = districtService.createDistrict(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Distrito creado exitosamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminDistrictDTO>> updateDistrict(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateDistrictDTO updateDTO) {
        AdminDistrictDTO updated = districtService.updateDistrict(id, updateDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Distrito actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteDistrict(@PathVariable Integer id) {
        districtService.deleteDistrict(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Distrito eliminado exitosamente", null));
    }
}