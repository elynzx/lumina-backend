package com.lumina.luminabackend.controller.admin;

import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.venue.AdminVenueDTO;
import com.lumina.luminabackend.dto.venue.CreateVenueDTO;
import com.lumina.luminabackend.dto.venue.UpdateVenueDTO;
import com.lumina.luminabackend.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/venues")
@RequiredArgsConstructor
public class AdminVenueController {

    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AdminVenueDTO>>> getAllForAdmin() {
        List<AdminVenueDTO> venues = venueService.findAllForAdmin();
        return ResponseEntity.ok(ApiResponseDTO.success("Lista de locales obtenida", venues));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminVenueDTO>> getByIdForAdmin(@PathVariable Integer id) {
        AdminVenueDTO venue = venueService.findByIdForAdmin(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Local encontrado", venue));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<AdminVenueDTO>>> searchByName(@RequestParam String name) {
        List<AdminVenueDTO> venues = venueService.searchByName(name);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", venues));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<AdminVenueDTO>> createVenue(
            @Valid @RequestBody CreateVenueDTO createDTO) {
        AdminVenueDTO created = venueService.createVenue(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Local creado exitosamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminVenueDTO>> updateVenue(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateVenueDTO updateDTO) {
        AdminVenueDTO updated = venueService.updateVenue(id, updateDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Local actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteVenue(@PathVariable Integer id) {
        venueService.deleteVenue(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Local eliminado exitosamente", null));
    }
}