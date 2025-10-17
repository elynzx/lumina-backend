package com.lumina.luminabackend.dto.reservation;

import com.lumina.luminabackend.dto.payment.PaymentDetailDTO;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class ReservationSuccessDTO {

    private Integer reservationId;
    private String confirmationCode;
    private LocalDateTime createdAt;
    private String status;

    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String duration;
    private Integer guestCount;
    private String eventTypeName;

    private String venueName;
    private String venueAddress;
    private String venueDistrict;
    private List<String> venuePhotos;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    private CostBreakdown costBreakdown;

    private List<FurnitureItemDetail> furnitureItems;

    private PaymentInfo paymentInfo;

    @Data
    @Builder
    public static class CostBreakdown {
        private Integer totalHours;
        private BigDecimal venueHourlyRate;
        private BigDecimal venueCost;
        private BigDecimal furnitureCost;
        private BigDecimal subtotal;
        private BigDecimal taxes;
        private BigDecimal totalCost;
    }

    @Data
    @Builder
    public static class FurnitureItemDetail {
        private String furnitureName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        private String photoUrl;
    }

    @Data
    @Builder
    public static class PaymentInfo {
        private String status; // PENDING, PARTIAL, PAID
        private BigDecimal paidAmount;
        private BigDecimal pendingAmount;
        private String nextPaymentDue;
        private List<PaymentDetailDTO> payments;
    }
}