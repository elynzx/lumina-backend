package com.lumina.luminabackend.service;

import com.lumina.luminabackend.dto.reservation.*;
import com.lumina.luminabackend.entity.event.EventType;
import com.lumina.luminabackend.entity.furniture.Furniture;
import com.lumina.luminabackend.entity.payment.Payment;
import com.lumina.luminabackend.entity.payment.PaymentMethod;
import com.lumina.luminabackend.entity.payment.PaymentStatus;
import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.reservation.ReservationFurniture;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import com.lumina.luminabackend.entity.user.User;
import com.lumina.luminabackend.entity.venue.Venue;
import com.lumina.luminabackend.entity.venue.VenuePhoto;
import com.lumina.luminabackend.repository.event.EventTypeRepository;
import com.lumina.luminabackend.repository.furniture.FurnitureRepository;
import com.lumina.luminabackend.repository.payment.PaymentMethodRepository;
import com.lumina.luminabackend.repository.payment.PaymentRepository;
import com.lumina.luminabackend.repository.reservation.ReservationRepository;
import com.lumina.luminabackend.repository.reservation.ReservationFurnitureRepository;
import com.lumina.luminabackend.repository.venue.VenueRepository;
import com.lumina.luminabackend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationFurnitureRepository reservationFurnitureRepository;
    private final VenueRepository venueRepository;
    private final EventTypeRepository eventTypeRepository;
    private final FurnitureRepository furnitureRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SecurityUtils securityUtils;

    public boolean checkAvailability(AvailabilityRequestDTO request) {
        List<Reservation> conflictingReservations = reservationRepository
                .findConflictingReservations(
                        request.getVenueId(),
                        request.getReservationDate(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        return conflictingReservations.isEmpty();
    }


    public BudgetCalculationDTO calculateBudget(BudgetCalculationDTO request) {

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue no encontrado"));

        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());
        int totalHours = Math.max(1, (int) duration.toHours());

        BigDecimal venueCost = venue.getPricePerHour().multiply(BigDecimal.valueOf(totalHours));

        BigDecimal furnitureCost = BigDecimal.ZERO;
        List<BudgetCalculationDTO.FurnitureItemDTO> furnitureItems = request.getFurnitureItems();

        if (furnitureItems != null && !furnitureItems.isEmpty()) {
            for (BudgetCalculationDTO.FurnitureItemDTO item : furnitureItems) {
                Furniture furniture = furnitureRepository.findById(item.getFurnitureId())
                        .orElseThrow(() -> new RuntimeException("Mobiliario no encontrado: " + item.getFurnitureId()));


                Integer usedQuantity = reservationFurnitureRepository
                        .findUsedQuantityByFurnitureAndDate(item.getFurnitureId(), request.getReservationDate());
                if (usedQuantity == null) usedQuantity = 0;

                int availableStock = furniture.getTotalStock() - usedQuantity;

                if (availableStock < item.getQuantity()) {
                    throw new RuntimeException("Stock insuficiente para: " + furniture.getFurnitureName() +
                            ". Disponible: " + availableStock + ", Solicitado: " + item.getQuantity());
                }

                BigDecimal subtotal = furniture.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                item.setUnitPrice(furniture.getUnitPrice());
                item.setSubtotal(subtotal);
                item.setFurnitureName(furniture.getFurnitureName());

                furnitureCost = furnitureCost.add(subtotal);
            }
        }

        // Total
        BigDecimal totalCost = venueCost.add(furnitureCost);

        return BudgetCalculationDTO.builder()
                .venueId(venue.getVenueId())
                .venueName(venue.getVenueName())
                .reservationDate(request.getReservationDate())
                .startTime((LocalTime) request.getStartTime())
                .endTime(request.getEndTime())
                .guestCount(request.getGuestCount())
                .eventTypeId(request.getEventTypeId())
                .totalHours(totalHours)
                .venueHourlyRate(venue.getPricePerHour())
                .venueCost(venueCost)
                .furnitureCost(furnitureCost)
                .totalCost(totalCost)
                .furnitureItems(furnitureItems)
                .build();
    }


    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO request) {

        User currentUser = securityUtils.getCurrentUser();

        AvailabilityRequestDTO availabilityCheck = new AvailabilityRequestDTO();
        availabilityCheck.setVenueId(request.getVenueId());
        availabilityCheck.setReservationDate(request.getReservationDate());
        availabilityCheck.setStartTime(request.getStartTime());
        availabilityCheck.setEndTime(request.getEndTime());

        if (!checkAvailability(availabilityCheck)) {
            throw new RuntimeException("El local ya no está disponible en esa fecha/hora");
        }

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Local no encontrado"));

        EventType eventType = eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado"));


        if (request.getGuestCount() > venue.getMaxCapacity()) {
            throw new RuntimeException("La cantidad de personas (" + request.getGuestCount() +
                    ") excede la capacidad máxima del venue (" + venue.getMaxCapacity() + ")");
        }

        ReservationStatus status = ReservationStatus.PENDING;
        PaymentMethod paymentMethod = null;

        if (request.getPaymentMethodId() != null) {
            paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                    .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

            if (!paymentMethod.getMethodName().equalsIgnoreCase("Transferencia")) {
                status = ReservationStatus.CONFIRMED;
            }
        }

        Reservation reservation = Reservation.builder()
                .user(currentUser)
                .venue(venue)
                .eventType(eventType)
                .reservationDate(request.getReservationDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .guestCount(request.getGuestCount())
                .venueCost(request.getVenueCost())
                .furnitureCost(request.getFurnitureCost() != null ? request.getFurnitureCost() : BigDecimal.ZERO)
                .totalCost(request.getTotalCost())
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();

        reservation = reservationRepository.save(reservation);


        if (request.getFurnitureItems() != null && !request.getFurnitureItems().isEmpty()) {
            for (ReservationRequestDTO.FurnitureItemRequestDTO furnitureItem : request.getFurnitureItems()) {
                Furniture furniture = furnitureRepository.findById(furnitureItem.getFurnitureId())
                        .orElseThrow(() -> new RuntimeException("Mobiliario no encontrado"));


                Integer usedQuantity = reservationFurnitureRepository
                        .findUsedQuantityByFurnitureAndDate(furnitureItem.getFurnitureId(), request.getReservationDate());
                if (usedQuantity == null) usedQuantity = 0;

                Integer availableStock = furniture.getTotalStock() - usedQuantity;

                if (availableStock < furnitureItem.getQuantity()) {
                    throw new RuntimeException("Stock insuficiente para: " + furniture.getFurnitureName());
                }


                ReservationFurniture reservationFurniture = ReservationFurniture.builder()
                        .reservationId(reservation.getReservationId())
                        .furnitureId(furniture.getFurnitureId())
                        .quantity(furnitureItem.getQuantity())
                        .unitPrice(furnitureItem.getUnitPrice())
                        .subtotal(furnitureItem.getSubtotal())
                        .build();


                reservationFurnitureRepository.save(reservationFurniture);
            }
        }

        if (request.getPaymentMethodId() != null) {
            if (paymentMethod == null) {
                paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                        .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
            }

            PaymentStatus paymentStatus = PaymentStatus.PENDING;
            if (paymentMethod.getMethodName().equalsIgnoreCase("Tarjeta") ||
                    paymentMethod.getMethodName().equalsIgnoreCase("PagoEfectivo")) {
                paymentStatus = PaymentStatus.PAID;
            }

            Payment payment = Payment.builder()
                    .reservation(reservation)
                    .paymentMethod(paymentMethod)
                    .amount(request.getTotalCost())
                    .status(paymentStatus)
                    .paymentDate(LocalDateTime.now())
                    .confirmationCode(null)
                    .receiptUrl(request.getPaymentReceiptUrl())
                    .build();

            paymentRepository.save(payment);
        }

        return mapToResponseDTO(reservation);
    }

    public List<ReservationResponseDTO> getMyReservations() {
        User currentUser = securityUtils.getCurrentUser();

        List<Reservation> reservations = reservationRepository
                .findByUserOrderByCreatedAtDesc(currentUser);

        return reservations.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ReservationSuccessDTO getReservationDetails(Integer reservationId) {
        User currentUser = securityUtils.getCurrentUser();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reservation.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("No tiene permisos para ver esta reserva");
        }

        Duration duration = Duration.between(reservation.getStartTime(), reservation.getEndTime());
        String durationText = duration.toHours() + " horas";

        String confirmationCode = "RES-" + reservation.getReservationId() + "-" +
                reservation.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<String> venuePhotos = new ArrayList<>();
        if (reservation.getVenue().getPhotos() != null) {
            venuePhotos = reservation.getVenue().getPhotos().stream()
                    .map(VenuePhoto::getPhotoUrl)
                    .limit(3)
                    .collect(Collectors.toList());
        }

        return ReservationSuccessDTO.builder()
                .reservationId(reservation.getReservationId())
                .confirmationCode(confirmationCode)
                .createdAt(reservation.getCreatedAt())
                .status(reservation.getStatus().getValue())
                .eventDate(reservation.getReservationDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .duration(durationText)
                .guestCount(reservation.getGuestCount())
                .eventTypeName(reservation.getEventType().getEventTypeName())
                .venueName(reservation.getVenue().getVenueName())
                .venueAddress(reservation.getVenue().getAddress())
                .venueDistrict(reservation.getVenue().getDistrict().getDistrictName())
                .venuePhotos(venuePhotos)
                .customerName(currentUser.getFirstName() + " " + currentUser.getLastName())
                .customerEmail(currentUser.getEmail())
                .customerPhone(currentUser.getPhone())
                .costBreakdown(buildCostBreakdown(reservation))
                .furnitureItems(buildFurnitureItemsFromRepository(reservationId))
                .paymentInfo(buildPaymentInfo(reservation))
                .build();
    }


    @Transactional
    public ReservationResponseDTO cancelReservation(Integer reservationId) {
        User currentUser = securityUtils.getCurrentUser();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reservation.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("No tiene permisos para cancelar esta reserva");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("La reserva ya está cancelada");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation = reservationRepository.save(reservation);

        return mapToResponseDTO(reservation);
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
                .paymentReceiptUrl(paymentReceiptUrl)
                .paymentMethodName(paymentMethodName)
                .furnitureItems(furnitureDTOs)
                .build();
    }

    private ReservationSuccessDTO.CostBreakdown buildCostBreakdown(Reservation reservation) {
        Duration duration = Duration.between(reservation.getStartTime(), reservation.getEndTime());
        int totalHours = Math.max(1, (int) duration.toHours());

        BigDecimal subtotal = reservation.getVenueCost().add(
                reservation.getFurnitureCost() != null ? reservation.getFurnitureCost() : BigDecimal.ZERO);
        BigDecimal taxes = subtotal.multiply(BigDecimal.valueOf(0.18));

        return ReservationSuccessDTO.CostBreakdown.builder()
                .totalHours(totalHours)
                .venueHourlyRate(reservation.getVenue().getPricePerHour())
                .venueCost(reservation.getVenueCost())
                .furnitureCost(reservation.getFurnitureCost() != null ? reservation.getFurnitureCost() : BigDecimal.ZERO)
                .subtotal(subtotal)
                .taxes(taxes)
                .totalCost(reservation.getTotalCost())
                .build();
    }


    private List<ReservationSuccessDTO.FurnitureItemDetail> buildFurnitureItemsFromRepository(Integer reservationId) {
        List<Object[]> furnitureDetails = reservationFurnitureRepository
                .findFurnitureDetailsByReservation(reservationId);

        return furnitureDetails.stream()
                .map(row -> ReservationSuccessDTO.FurnitureItemDetail.builder()
                        .furnitureName((String) row[1])
                        .quantity((Integer) row[2])
                        .unitPrice((BigDecimal) row[3])
                        .subtotal((BigDecimal) row[4])
                        .photoUrl((String) row[5])
                        .build())
                .collect(Collectors.toList());
    }

    private ReservationSuccessDTO.PaymentInfo buildPaymentInfo(Reservation reservation) {

        return ReservationSuccessDTO.PaymentInfo.builder()
                .status("PENDING")
                .paidAmount(BigDecimal.ZERO)
                .pendingAmount(reservation.getTotalCost())
                .nextPaymentDue("Inmediato")
                .payments(new ArrayList<>())
                .build();
    }
}