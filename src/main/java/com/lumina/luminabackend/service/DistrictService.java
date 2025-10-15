package com.lumina.luminabackend.service;

import com.lumina.luminabackend.dto.district.*;
import com.lumina.luminabackend.entity.district.District;
import com.lumina.luminabackend.exception.DuplicateResourceException;
import com.lumina.luminabackend.exception.ResourceNotFoundException;
import com.lumina.luminabackend.repository.district.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class DistrictService {

    private final DistrictRepository districtRepository;

    /**
     * Client methods
     */
    @Transactional(readOnly = true)
    public List<DistrictDTO> findAll() {
        return districtRepository.findAll()
                .stream()
                .map(this::convertToClientDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DistrictCardDTO> findDistrictsWithVenueCount() {
        List<Object[]> results = districtRepository.findDistrictsWithVenueCount();
        return results.stream()
                .map(row -> DistrictCardDTO.builder()
                        .districtId((Integer) row[0])
                        .districtName((String) row[1])
                        .photoUrl((String) row[2] != null ? (String) row[2] : "default-district.jpg")
                        .venueCount(((Number) row[3]).intValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DistrictDTO findById(Integer id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con ID: " + id));
        return convertToClientDTO(district);
    }

    /**
     * Admin methods
     */

    @Transactional(readOnly = true)
    public List<AdminDistrictDTO> findAllForAdmin() {
        return districtRepository.findAll()
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminDistrictDTO findByIdForAdmin(Integer id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con ID: " + id));
        return convertToAdminDTO(district);
    }

    @Transactional(readOnly = true)
    public List<AdminDistrictDTO> searchByName(String name) {
        return districtRepository.findByDistrictNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    public AdminDistrictDTO createDistrict(CreateDistrictDTO createDTO) {
        if (districtRepository.existsByDistrictNameIgnoreCase(createDTO.getDistrictName())) {
            throw new DuplicateResourceException("Ya existe un distrito con el nombre: " + createDTO.getDistrictName());
        }

        District district = District.builder()
                .districtName(createDTO.getDistrictName())
                .build();

        return convertToAdminDTO(districtRepository.save(district));
    }

    public AdminDistrictDTO updateDistrict(Integer id, UpdateDistrictDTO updateDTO) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con ID: " + id));

        if (updateDTO.getDistrictName() != null) {

            if (districtRepository.existsByDistrictNameIgnoreCase(updateDTO.getDistrictName()) &&
                    !district.getDistrictName().equalsIgnoreCase(updateDTO.getDistrictName())) {
                throw new DuplicateResourceException("Ya existe un distrito con el nombre: " + updateDTO.getDistrictName());
            }
            district.setDistrictName(updateDTO.getDistrictName());
        }

        return convertToAdminDTO(districtRepository.save(district));
    }

    public void deleteDistrict(Integer id) {
        if (!districtRepository.existsById(id)) {
            throw new ResourceNotFoundException("Distrito no encontrado con ID: " + id);
        }
        districtRepository.deleteById(id);
    }

    /**
     * Private methods
     */
    private DistrictDTO convertToClientDTO(District district) {
        return DistrictDTO.builder()
                .districtId(district.getDistrictId())
                .districtName(district.getDistrictName())
                .build();
    }

    private AdminDistrictDTO convertToAdminDTO(District district) {
        return AdminDistrictDTO.builder()
                .districtId(district.getDistrictId())
                .districtName(district.getDistrictName())
                .build();
    }
}