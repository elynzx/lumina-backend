package com.lumina.luminabackend.dto.reservation;
import com.lumina.luminabackend.dto.furniture.FurnitureCartDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Integer reservationId;
    private String venueName;
    private String eventTypeName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer guestCount;
    private BigDecimal venueCost;
    private BigDecimal furnitureCost;
    private BigDecimal totalCost;
    private String status; // PENDING, CONFIRMED, CANCELLED
    private LocalDateTime createdAt;
    private List<FurnitureCartDTO> furnitureItems;
}