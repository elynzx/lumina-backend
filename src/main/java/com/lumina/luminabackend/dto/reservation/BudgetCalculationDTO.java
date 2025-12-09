package com.lumina.luminabackend.dto.reservation;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class BudgetCalculationDTO {

    private Integer venueId;
    private String venueName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer guestCount;
    private Integer eventTypeId;
    private List<FurnitureItemDTO> furnitureItems;

    private Integer totalHours;
    private BigDecimal venueHourlyRate;
    private BigDecimal venueCost;
    private BigDecimal furnitureCost;
    private BigDecimal totalCost;

    @Data
    public static class FurnitureItemDTO {
        private Integer furnitureId;
        private String furnitureName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }
}