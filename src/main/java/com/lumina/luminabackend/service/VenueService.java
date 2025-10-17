package com.lumina.luminabackend.service;


import com.lumina.luminabackend.dto.venue.*;
import com.lumina.luminabackend.entity.district.District;
import com.lumina.luminabackend.entity.venue.Venue;
import com.lumina.luminabackend.entity.venue.VenuePhoto;
import com.lumina.luminabackend.entity.venue.VenueStatus;
import com.lumina.luminabackend.exception.DuplicateResourceException;
import com.lumina.luminabackend.exception.ResourceNotFoundException;
import com.lumina.luminabackend.repository.district.DistrictRepository;
import com.lumina.luminabackend.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VenueService {

    private final VenueRepository venueRepository;
    private final DistrictRepository districtRepository;

    /**
     * Client methods
     */
    @Transactional(readOnly = true)
    public List<VenueCardDTO> findAllAvailable() {
        return venueRepository.findAvailable(VenueStatus.AVAILABLE)
                .stream()
                .map(this::convertToCardDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VenueDetailDTO findById(Integer id) {
        Venue venue = venueRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con ID: " + id));
        return convertToDetailDTO(venue);
    }

    @Transactional(readOnly = true)
    public List<VenueSliderDTO> findFeaturedVenues() {
        return venueRepository.findAvailable(VenueStatus.AVAILABLE)
                .stream()
                .limit(5)
                .map(this::convertToSliderDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VenueCardDTO> searchWithFilters(VenueFilterDTO filters) {
        return venueRepository.findWithFilters(
                VenueStatus.AVAILABLE,
                        filters.getDistrictId(),
                        filters.getEventTypeId(),
                        filters.getMinCapacity(),
                        filters.getMinPrice(),
                        filters.getMaxPrice()
                ).stream()
                .map(this::convertToCardDTO)
                .collect(Collectors.toList());
    }


    /**
     * Admin methods
     */
    @Transactional(readOnly = true)
    public List<AdminVenueDTO> findAllForAdmin() {
        return venueRepository.findAllWithDetails()
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminVenueDTO findByIdForAdmin(Integer id) {
        Venue venue = venueRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con ID: " + id));
        return convertToAdminDTO(venue);
    }

    @Transactional(readOnly = true)
    public List<AdminVenueDTO> searchByName(String name) {
        return venueRepository.findByVenueNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    public AdminVenueDTO createVenue(CreateVenueDTO createDTO) {
        if (venueRepository.existsByVenueNameIgnoreCase(createDTO.getVenueName())) {
            throw new DuplicateResourceException("Ya existe un local con el nombre: " + createDTO.getVenueName());
        }

        District district = districtRepository.findById(createDTO.getDistrictId())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con ID: " + createDTO.getDistrictId()));

        Venue venue = Venue.builder()
                .venueName(createDTO.getVenueName())
                .address(createDTO.getAddress())
                .district(district)
                .maxCapacity(createDTO.getMaxCapacity())
                .pricePerHour(createDTO.getPricePerHour())
                .description(createDTO.getDescription())
                .status(VenueStatus.AVAILABLE)
                .build();

        return convertToAdminDTO(venueRepository.save(venue));
    }

    public AdminVenueDTO updateVenue(Integer id, UpdateVenueDTO updateDTO) {
        Venue venue = venueRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con ID: " + id));

        if (updateDTO.getVenueName() != null) {
            if (venueRepository.existsByVenueNameIgnoreCase(updateDTO.getVenueName()) &&
                    !venue.getVenueName().equalsIgnoreCase(updateDTO.getVenueName())) {
                throw new DuplicateResourceException("Ya existe un local con el nombre: " + updateDTO.getVenueName());
            }
            venue.setVenueName(updateDTO.getVenueName());
        }

        if (updateDTO.getAddress() != null) {
            venue.setAddress(updateDTO.getAddress());
        }

        if (updateDTO.getDistrictId() != null) {
            District district = districtRepository.findById(updateDTO.getDistrictId())
                    .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con ID: " + updateDTO.getDistrictId()));
            venue.setDistrict(district);
        }

        if (updateDTO.getMaxCapacity() != null) {
            venue.setMaxCapacity(updateDTO.getMaxCapacity());
        }

        if (updateDTO.getPricePerHour() != null) {
            venue.setPricePerHour(updateDTO.getPricePerHour());
        }

        if (updateDTO.getDescription() != null) {
            venue.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getStatus() != null) {
            venue.setStatus(VenueStatus.valueOf(updateDTO.getStatus()));
        }

        return convertToAdminDTO(venueRepository.save(venue));
    }

    public void deleteVenue(Integer id) {
        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Local no encontrado con ID: " + id);
        }
        venueRepository.deleteById(id);
    }

    /**
     * Private methods
     */
    private VenueCardDTO convertToCardDTO(Venue venue) {
        return VenueCardDTO.builder()
                .venueId(venue.getVenueId())
                .venueName(venue.getVenueName())
                .address(venue.getAddress())
                .districtName(venue.getDistrict().getDistrictName())
                .maxCapacity(venue.getMaxCapacity())
                .pricePerHour(venue.getPricePerHour())
                .description(venue.getDescription())
                .mainPhotoUrl(getMainPhoto(venue))
                .status(venue.getStatus().getValue())
                .build();
    }

    private VenueDetailDTO convertToDetailDTO(Venue venue) {
        return VenueDetailDTO.builder()
                .venueId(venue.getVenueId())
                .venueName(venue.getVenueName())
                .address(venue.getAddress())
                .districtName(venue.getDistrict().getDistrictName())
                .maxCapacity(venue.getMaxCapacity())
                .pricePerHour(venue.getPricePerHour())
                .fullDescription(venue.getDescription())
                .photos(getAllPhotos(venue))
                .availableEventTypes(getEventTypes(venue))
                .status(venue.getStatus().getValue())
                .build();
    }

    private VenueSliderDTO convertToSliderDTO(Venue venue) {
        return VenueSliderDTO.builder()
                .venueId(venue.getVenueId())
                .venueName(venue.getVenueName())
                .mainPhoto(getMainPhoto(venue))
                .build();
    }

    private AdminVenueDTO convertToAdminDTO(Venue venue) {
        return AdminVenueDTO.builder()
                .venueId(venue.getVenueId())
                .venueName(venue.getVenueName())
                .address(venue.getAddress())
                .districtId(venue.getDistrict().getDistrictId())
                .districtName(venue.getDistrict().getDistrictName())
                .maxCapacity(venue.getMaxCapacity())
                .pricePerHour(venue.getPricePerHour())
                .description(venue.getDescription())
                .mainPhotoUrl(getMainPhoto(venue))
                .photos(String.join(",", getAllPhotos(venue)))
                .availableEventTypes(String.join(",", getEventTypes(venue)))
                .status(venue.getStatus().name())
                .build();
    }

    /**
     * Helper
     */
    private String getMainPhoto(Venue venue) {
        if (venue.getPhotos() == null || venue.getPhotos().isEmpty()) {
            return "default-venue.jpg";
        }
        return venue.getPhotos().get(0).getPhotoUrl();
    }

    private List<String> getAllPhotos(Venue venue) {
        if (venue.getPhotos() == null || venue.getPhotos().isEmpty()) {
            return List.of("default-venue.jpg");
        }
        return venue.getPhotos().stream()
                .map(photo -> photo.getPhotoUrl())
                .collect(Collectors.toList());
    }

    private List<String> getEventTypes(Venue venue) {
        if (venue.getVenueEventTypes() == null || venue.getVenueEventTypes().isEmpty()) {
            return List.of();
        }
        return venue.getVenueEventTypes().stream()
                .map(vet -> vet.getEventType().getEventTypeName())
                .collect(Collectors.toList());
    }
}