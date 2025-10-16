package com.lumina.luminabackend.dto.reservation;

import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class ReservationResponseDTO {
    private Integer reservationId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer guestCount;
    private BigDecimal venueCost;
    private BigDecimal furnitureCost;
    private BigDecimal totalCost;
    private ReservationStatus status;
    private LocalDateTime createdAt;

    private Integer venueId;
    private String venueName;
    private String venueAddress;

    private Integer eventTypeId;
    private String eventTypeName;

    private List<FurnitureItemDTO> furnitureItems;

    @Data
    @Builder
    public static class FurnitureItemDTO {
        private Integer furnitureId;
        private String furnitureName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }
}