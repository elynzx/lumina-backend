package com.lumina.luminabackend.controller.admin;

import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.event.AdminEventTypeDTO;
import com.lumina.luminabackend.dto.event.CreateEventTypeDTO;
import com.lumina.luminabackend.dto.event.UpdateEventTypeDTO;
import com.lumina.luminabackend.service.EventTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/event-types")
@RequiredArgsConstructor
public class AdminEventTypeController {

    private final EventTypeService eventTypeService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AdminEventTypeDTO>>> getAllForAdmin() {
        List<AdminEventTypeDTO> eventTypes = eventTypeService.findAllForAdmin();
        return ResponseEntity.ok(ApiResponseDTO.success("Lista de tipos de evento obtenida", eventTypes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminEventTypeDTO>> getByIdForAdmin(@PathVariable Integer id) {
        AdminEventTypeDTO eventType = eventTypeService.findByIdForAdmin(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Tipo de evento encontrado", eventType));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<AdminEventTypeDTO>>> searchByName(@RequestParam String name) {
        List<AdminEventTypeDTO> eventTypes = eventTypeService.searchByName(name);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", eventTypes));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<AdminEventTypeDTO>> createEventType(
            @Valid @RequestBody CreateEventTypeDTO createDTO) {
        AdminEventTypeDTO created = eventTypeService.createEventType(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Tipo de evento creado exitosamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminEventTypeDTO>> updateEventType(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateEventTypeDTO updateDTO) {
        AdminEventTypeDTO updated = eventTypeService.updateEventType(id, updateDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Tipo de evento actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteEventType(@PathVariable Integer id) {
        eventTypeService.deleteEventType(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Tipo de evento eliminado exitosamente", null));
    }
}