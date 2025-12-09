package com.lumina.luminabackend.service;


import com.lumina.luminabackend.dto.venue.*;
import com.lumina.luminabackend.entity.district.District;
import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.venue.Venue;

import com.lumina.luminabackend.entity.venue.VenueStatus;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import com.lumina.luminabackend.exception.DuplicateResourceException;
import com.lumina.luminabackend.exception.ResourceNotFoundException;
import com.lumina.luminabackend.exception.BusinessException;
import com.lumina.luminabackend.repository.district.DistrictRepository;
import com.lumina.luminabackend.repository.venue.VenueRepository;
import com.lumina.luminabackend.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VenueService {

    private final VenueRepository venueRepository;
    private final DistrictRepository districtRepository;
    private final ReservationRepository reservationRepository;

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
        List<Venue> venues;

        if (filters.getEventTypeId() != null) {
            venues = venueRepository.findWithEventTypeFilter(
                    VenueStatus.AVAILABLE,
                    filters.getDistrictId(),
                    filters.getEventTypeId(),
                    filters.getMinCapacity(),
                    filters.getMaxCapacity(),
                    filters.getMinPrice(),
                    filters.getMaxPrice()
            );
        } else {
            venues = venueRepository.findWithoutEventTypeFilter(
                    VenueStatus.AVAILABLE,
                    filters.getDistrictId(),
                    filters.getMinCapacity(),
                    filters.getMaxCapacity(),
                    filters.getMinPrice(),
                    filters.getMaxPrice()
            );
        }

        if (!venues.isEmpty()) {
            List<Integer> venueIds = venues.stream()
                    .map(Venue::getVenueId)
                    .toList();

            venueRepository.loadEventTypesByVenueIds(venueIds);
        }

        return venues.stream()
                .map(this::convertToCardDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LocalDate> getUnavailableDates(Integer venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new ResourceNotFoundException("Local no encontrado con ID: " + venueId);
        }

        return reservationRepository.findByVenueVenueIdAndStatusIn(
                        venueId,
                        List.of(ReservationStatus.CONFIRMED, ReservationStatus.PENDING)
                ).stream()
                .map(Reservation::getReservationDate)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Admin methods
     */
    @Transactional(readOnly = true)
    public List<AdminVenueDTO> findAllForAdmin() {

        List<Venue> venues = venueRepository.findAllWithDetails();

        if (!venues.isEmpty()) {
            venueRepository.findAllWithEventTypes();
        }

        return venues.stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminVenueDTO findByIdForAdmin(Integer id) {
        Venue venue = venueRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con ID: " + id));

        venueRepository.findByIdWithEventTypes(id);

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
                .latitude(createDTO.getLatitude())
                .longitude(createDTO.getLongitude())
                .googleMapsUrl(createDTO.getGoogleMapsUrl())
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
        if (updateDTO.getLatitude() != null) {
            venue.setLatitude(updateDTO.getLatitude());
        }

        if (updateDTO.getLongitude() != null) {
            venue.setLongitude(updateDTO.getLongitude());
        }

        if (updateDTO.getGoogleMapsUrl() != null) {
            venue.setGoogleMapsUrl(updateDTO.getGoogleMapsUrl());
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

        long confirmedReservations = reservationRepository.countByVenueVenueIdAndStatus(id, ReservationStatus.CONFIRMED);
        if (confirmedReservations > 0) {
            throw new BusinessException("No se puede eliminar el local porque tiene " + confirmedReservations + " reserva(s) confirmada(s)");
        }

        long pendingReservations = reservationRepository.countByVenueVenueIdAndStatus(id, ReservationStatus.PENDING);
        if (pendingReservations > 0) {
            throw new BusinessException("No se puede eliminar el local porque tiene " + pendingReservations + " reserva(s) pendiente(s)");
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
                .latitude(venue.getLatitude())
                .longitude(venue.getLongitude())
                .googleMapsUrl(venue.getGoogleMapsUrl())
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
                .availableEventTypeIds(String.join(",", getEventTypeIds(venue)))
                .latitude(venue.getLatitude())
                .longitude(venue.getLongitude())
                .googleMapsUrl(venue.getGoogleMapsUrl())
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

    private List<String> getEventTypeIds(Venue venue) {
        if (venue.getVenueEventTypes() == null || venue.getVenueEventTypes().isEmpty()) {
            return List.of();
        }
        return venue.getVenueEventTypes().stream()
                .map(vet -> String.valueOf(vet.getEventType().getEventTypeId()))
                .collect(Collectors.toList());
    }
}