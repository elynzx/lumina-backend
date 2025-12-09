package com.lumina.luminabackend.dto.reservation;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ReservationRequestDTO {
    private Integer venueId;
    private Integer eventTypeId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer guestCount;
    private BigDecimal venueCost;
    private BigDecimal furnitureCost;
    private BigDecimal totalCost;
    private List<FurnitureItemRequestDTO> furnitureItems;
    private Integer paymentMethodId;
    private String approvalCode;
    private String paymentReceiptUrl;

    @Data
    public static class FurnitureItemRequestDTO {
        private Integer furnitureId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }
}