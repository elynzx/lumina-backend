package com.lumina.luminabackend.service;

import com.lumina.luminabackend.dto.furniture.AdminFurnitureDTO;
import com.lumina.luminabackend.dto.furniture.CreateFurnitureDTO;
import com.lumina.luminabackend.dto.furniture.FurnitureDTO;
import com.lumina.luminabackend.dto.furniture.UpdateFurnitureDTO;
import com.lumina.luminabackend.entity.furniture.Furniture;
import com.lumina.luminabackend.exception.ResourceNotFoundException;
import com.lumina.luminabackend.repository.furniture.FurnitureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FurnitureService {

    private final FurnitureRepository furnitureRepository;

    /**
     * Client methods
     */
    @Transactional(readOnly = true)
    public List<FurnitureDTO> findAllAvailable() {
        return furnitureRepository.findAvailable()
                .stream()
                .map(this::convertToClientDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FurnitureDTO findById(Integer id) {
        Furniture furniture = furnitureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mobiliario no encontrado con ID: " + id));
        return convertToClientDTO(furniture);
    }

    @Transactional(readOnly = true)
    public boolean checkAvailability(Integer furnitureId, Integer requestedQuantity, LocalDate date) {
        if (!furnitureRepository.existsById(furnitureId)) {
            return false;
        }

        Integer totalStock = furnitureRepository.findAvailableStock(furnitureId);
        if (totalStock == null || totalStock < requestedQuantity) {
            return false;
        }

        Integer usedQuantity = furnitureRepository.findUsedQuantityByFurnitureAndDate(furnitureId, date);
        Integer availableForDate = totalStock - (usedQuantity != null ? usedQuantity : 0);

        return availableForDate >= requestedQuantity;
    }

    /**
     * Admin methods
     */

    @Transactional(readOnly = true)
    public List<AdminFurnitureDTO> findAllForAdmin() {
        return furnitureRepository.findAll()
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminFurnitureDTO findByIdForAdmin(Integer id) {
        Furniture furniture = furnitureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mobiliario no encontrado con ID: " + id));
        return convertToAdminDTO(furniture);
    }

    @Transactional(readOnly = true)
    public List<AdminFurnitureDTO> searchByName(String name) {
        return furnitureRepository.findByFurnitureNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }


    public AdminFurnitureDTO createFurniture(CreateFurnitureDTO createDTO) {
        Furniture furniture = Furniture.builder()
                .furnitureName(createDTO.getFurnitureName())
                .description(createDTO.getDescription())
                .totalStock(createDTO.getTotalStock())
                .unitPrice(createDTO.getUnitPrice())
                .photoUrl(createDTO.getPhotoUrl())
                .build();

        Furniture saved = furnitureRepository.save(furniture);
        return convertToAdminDTO(saved);
    }

    public AdminFurnitureDTO updateFurniture(Integer id, UpdateFurnitureDTO updateDTO) {
        Furniture furniture = furnitureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mobiliario no encontrado con ID: " + id));

        if (updateDTO.getFurnitureName() != null) {
            furniture.setFurnitureName(updateDTO.getFurnitureName());
        }
        if (updateDTO.getDescription() != null) {
            furniture.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getTotalStock() != null) {
            furniture.setTotalStock(updateDTO.getTotalStock());
        }
        if (updateDTO.getUnitPrice() != null) {
            furniture.setUnitPrice(updateDTO.getUnitPrice());
        }
        if (updateDTO.getPhotoUrl() != null) {
            furniture.setPhotoUrl(updateDTO.getPhotoUrl());
        }

        return convertToAdminDTO(furnitureRepository.save(furniture));
    }

    public void deleteFurniture(Integer id) {
        if (!furnitureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mobiliario no encontrado con ID: " + id);
        }
        furnitureRepository.deleteById(id);
    }
    /**
     * Private methods
     */
    private FurnitureDTO convertToClientDTO(Furniture furniture) {
        return FurnitureDTO.builder()
                .furnitureId(furniture.getFurnitureId())
                .furnitureName(furniture.getFurnitureName())
                .description(furniture.getDescription())
                .unitPrice(furniture.getUnitPrice())
                .photoUrl(furniture.getPhotoUrl())
                .totalStock(furniture.getTotalStock())
                .build();
    }

    private AdminFurnitureDTO convertToAdminDTO(Furniture furniture) {
        return AdminFurnitureDTO.builder()
                .furnitureId(furniture.getFurnitureId())
                .furnitureName(furniture.getFurnitureName())
                .description(furniture.getDescription())
                .totalStock(furniture.getTotalStock())
                .unitPrice(furniture.getUnitPrice())
                .photoUrl(furniture.getPhotoUrl())
                .build();
    }


}