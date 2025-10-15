package com.lumina.luminabackend.controller.customer;


import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.district.DistrictCardDTO;
import com.lumina.luminabackend.dto.district.DistrictDTO;
import com.lumina.luminabackend.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<DistrictDTO>>> getAll() {
        List<DistrictDTO> districts = districtService.findAll();
        return ResponseEntity.ok(ApiResponseDTO.success("Distritos encontrados", districts));
    }

    @GetMapping("/cards")
    public ResponseEntity<ApiResponseDTO<List<DistrictCardDTO>>> getDistrictsWithVenueCount() {
        List<DistrictCardDTO> districts = districtService.findDistrictsWithVenueCount();
        return ResponseEntity.ok(ApiResponseDTO.success("Distritos con locales encontrados", districts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DistrictDTO>> getById(@PathVariable Integer id) {
        DistrictDTO district = districtService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Distrito encontrado", district));
    }
}