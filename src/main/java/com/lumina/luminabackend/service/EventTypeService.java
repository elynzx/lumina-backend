package com.lumina.luminabackend.service;

import com.lumina.luminabackend.dto.event.AdminEventTypeDTO;
import com.lumina.luminabackend.dto.event.CreateEventTypeDTO;
import com.lumina.luminabackend.dto.event.EventTypeDTO;
import com.lumina.luminabackend.dto.event.UpdateEventTypeDTO;
import com.lumina.luminabackend.entity.event.EventType;
import com.lumina.luminabackend.exception.DuplicateResourceException;
import com.lumina.luminabackend.exception.ResourceNotFoundException;
import com.lumina.luminabackend.repository.event.EventTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    /**
     * Client methods
     */
    @Transactional(readOnly = true)
    public List<EventTypeDTO> findAll() {
        return eventTypeRepository.findAll()
                .stream()
                .map(this::convertToClientDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventTypeDTO findById(Integer id) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de evento no encontrado con ID: " + id));
        return convertToClientDTO(eventType);
    }

    /**
     * Admin methods
     */
    @Transactional(readOnly = true)
    public List<AdminEventTypeDTO> findAllForAdmin() {
        return eventTypeRepository.findAll()
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminEventTypeDTO findByIdForAdmin(Integer id) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de evento no encontrado con ID: " + id));
        return convertToAdminDTO(eventType);
    }

    @Transactional(readOnly = true)
    public List<AdminEventTypeDTO> searchByName(String name) {
        return eventTypeRepository.findByEventTypeNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    public AdminEventTypeDTO createEventType(CreateEventTypeDTO createDTO) {
        if (eventTypeRepository.existsByEventTypeNameIgnoreCase(createDTO.getEventTypeName())) {
            throw new DuplicateResourceException("Ya existe un tipo de evento con el nombre: " + createDTO.getEventTypeName());
        }

        EventType eventType = EventType.builder()
                .eventTypeName(createDTO.getEventTypeName())
                .description(createDTO.getDescription())
                .photoUrl(createDTO.getPhotoUrl())
                .build();

        return convertToAdminDTO(eventTypeRepository.save(eventType));
    }

    public AdminEventTypeDTO updateEventType(Integer id, UpdateEventTypeDTO updateDTO) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de evento no encontrado con ID: " + id));

        if (updateDTO.getEventTypeName() != null) {
            // Verificar que no existe otro tipo de evento con el mismo nombre
            if (eventTypeRepository.existsByEventTypeNameIgnoreCase(updateDTO.getEventTypeName()) &&
                    !eventType.getEventTypeName().equalsIgnoreCase(updateDTO.getEventTypeName())) {
                throw new DuplicateResourceException("Ya existe un tipo de evento con el nombre: " + updateDTO.getEventTypeName());
            }
            eventType.setEventTypeName(updateDTO.getEventTypeName());
        }

        if (updateDTO.getDescription() != null) {
            eventType.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPhotoUrl() != null) {
            eventType.setPhotoUrl(updateDTO.getPhotoUrl());
        }
        return convertToAdminDTO(eventTypeRepository.save(eventType));
    }

    public void deleteEventType(Integer id) {
        if (!eventTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de evento no encontrado con ID: " + id);
        }
        eventTypeRepository.deleteById(id);
    }

    /**
     * Private methods
     */
    private EventTypeDTO convertToClientDTO(EventType eventType) {
        return EventTypeDTO.builder()
                .eventTypeId(eventType.getEventTypeId())
                .eventTypeName(eventType.getEventTypeName())
                .description(eventType.getDescription())
                .photoUrl(eventType.getPhotoUrl())
                .build();
    }

    private AdminEventTypeDTO convertToAdminDTO(EventType eventType) {
        return AdminEventTypeDTO.builder()
                .eventTypeId(eventType.getEventTypeId())
                .eventTypeName(eventType.getEventTypeName())
                .description(eventType.getDescription())
                .photoUrl(eventType.getPhotoUrl())
                .build();
    }
}