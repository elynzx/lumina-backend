package com.lumina.luminabackend.controller.customer;


import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.event.EventTypeDTO;
import com.lumina.luminabackend.service.EventTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/event-types")
@RequiredArgsConstructor
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<EventTypeDTO>>> getAll() {
        List<EventTypeDTO> eventTypes = eventTypeService.findAll();
        return ResponseEntity.ok(ApiResponseDTO.success("Tipos de evento encontrados", eventTypes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<EventTypeDTO>> getById(@PathVariable Integer id) {
        EventTypeDTO eventType = eventTypeService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Tipo de evento encontrado", eventType));
    }
}