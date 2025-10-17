package com.lumina.luminabackend.controller.customer;

import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.venue.VenueCardDTO;
import com.lumina.luminabackend.dto.venue.VenueDetailDTO;
import com.lumina.luminabackend.dto.venue.VenueFilterDTO;
import com.lumina.luminabackend.dto.venue.VenueSliderDTO;
import com.lumina.luminabackend.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<VenueCardDTO>>> getAllAvailable() {
        List<VenueCardDTO> venues = venueService.findAllAvailable();
        return ResponseEntity.ok(ApiResponseDTO.success("Locales disponibles encontrados", venues));
    }

    @GetMapping("/slider")
    public ResponseEntity<ApiResponseDTO<List<VenueSliderDTO>>> getFeatured() {
        List<VenueSliderDTO> venues = venueService.findFeaturedVenues();
        return ResponseEntity.ok(ApiResponseDTO.success("Locales destacados encontrados", venues));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<VenueDetailDTO>> getById(@PathVariable Integer id) {
        VenueDetailDTO venue = venueService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Detalle del local encontrado", venue));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<VenueCardDTO>>> search(
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) Integer eventTypeId,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        VenueFilterDTO filters = VenueFilterDTO.builder()
                .districtId(districtId)
                .eventTypeId(eventTypeId)
                .minCapacity(minCapacity)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        List<VenueCardDTO> venues = venueService.searchWithFilters(filters);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", venues));
    }
}