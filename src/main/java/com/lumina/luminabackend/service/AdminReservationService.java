package com.lumina.luminabackend.service;

import com.lumina.luminabackend.dto.reservation.ReservationResponseDTO;
import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.reservation.ReservationFurniture;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import com.lumina.luminabackend.entity.payment.Payment;
import com.lumina.luminabackend.entity.payment.PaymentMethod;
import com.lumina.luminabackend.entity.payment.PaymentStatus;
import com.lumina.luminabackend.repository.reservation.ReservationFurnitureRepository;
import com.lumina.luminabackend.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationFurnitureRepository reservationFurnitureRepository;

    public List<ReservationResponseDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAllByOrderByCreatedAtDesc();
        return reservations.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO getReservationById(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
        return mapToResponseDTO(reservation);
    }

    @Transactional
    public ReservationResponseDTO updateReservationStatus(Integer id, ReservationStatus newStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

        // Validaciones de transición de estado
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("No se puede cambiar el estado de una reserva cancelada");
        }

        reservation.setStatus(newStatus);
        reservation = reservationRepository.save(reservation);

        return mapToResponseDTO(reservation);
    }

    public List<ReservationResponseDTO> searchReservations(
            ReservationStatus status,
            LocalDate startDate,
            LocalDate endDate,
            Integer venueId,
            Integer userId) {

        List<Reservation> reservations;

        // Si no hay filtros, devolver todas
        if (status == null && startDate == null && endDate == null && venueId == null && userId == null) {
            reservations = reservationRepository.findAllByOrderByCreatedAtDesc();
        } else {
            // Aplicar filtros
            reservations = reservationRepository.findAll().stream()
                    .filter(r -> status == null || r.getStatus() == status)
                    .filter(r -> startDate == null || !r.getReservationDate().isBefore(startDate))
                    .filter(r -> endDate == null || !r.getReservationDate().isAfter(endDate))
                    .filter(r -> venueId == null || r.getVenue().getVenueId().equals(venueId))
                    .filter(r -> userId == null || r.getUser().getUserId().equals(userId))
                    .collect(Collectors.toList());
        }

        return reservations.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByStatusOrderByCreatedAtDesc(status);
        return reservations.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getReservationStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        long totalReservations = reservationRepository.count();
        long pendingReservations = reservationRepository.countByStatus(ReservationStatus.PENDING);
        long confirmedReservations = reservationRepository.countByStatus(ReservationStatus.CONFIRMED);
        long cancelledReservations = reservationRepository.countByStatus(ReservationStatus.CANCELLED);

        statistics.put("totalReservations", totalReservations);
        statistics.put("pendingReservations", pendingReservations);
        statistics.put("confirmedReservations", confirmedReservations);
        statistics.put("cancelledReservations", cancelledReservations);

        return statistics;
    }
    private ReservationResponseDTO mapToResponseDTO(Reservation reservation) {
        List<ReservationFurniture> furnitureItems = reservationFurnitureRepository
                .findByReservationId(reservation.getReservationId());

        List<ReservationResponseDTO.FurnitureItemDTO> furnitureDTOs = furnitureItems.stream()
                .map(item -> ReservationResponseDTO.FurnitureItemDTO.builder()
                        .furnitureId(item.getFurnitureId())
                        .furnitureName(item.getFurniture() != null ?
                                item.getFurniture().getFurnitureName() : "Mobiliario " + item.getFurnitureId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        // Obtener información del pago (si existe)
        String paymentReceiptUrl = null;
        String paymentMethodName = null;

        if (reservation.getPayments() != null && !reservation.getPayments().isEmpty()) {
            Payment lastPayment = reservation.getPayments().get(0);
            paymentReceiptUrl = lastPayment.getReceiptUrl();
            paymentMethodName = lastPayment.getPaymentMethod().getMethodName();
        }

        return ReservationResponseDTO.builder()
                .reservationId(reservation.getReservationId())
                .reservationDate(reservation.getReservationDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .guestCount(reservation.getGuestCount())
                .venueCost(reservation.getVenueCost())
                .furnitureCost(reservation.getFurnitureCost())
                .totalCost(reservation.getTotalCost())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .venueId(reservation.getVenue().getVenueId())
                .venueName(reservation.getVenue().getVenueName())
                .venueAddress(reservation.getVenue().getAddress())
                .eventTypeId(reservation.getEventType().getEventTypeId())
                .eventTypeName(reservation.getEventType().getEventTypeName())
                .userId(reservation.getUser().getUserId())
                .customerName(reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName())
                .customerEmail(reservation.getUser().getEmail())
                .customerPhone(reservation.getUser().getPhone())
                .paymentReceiptUrl(paymentReceiptUrl)        // ← NUEVO
                .paymentMethodName(paymentMethodName)        // ← NUEVO
                .furnitureItems(furnitureDTOs)
                .build();
    }
}
