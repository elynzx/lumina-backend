package com.lumina.luminabackend.dto.dashboard;

import com.lumina.luminabackend.dto.reservation.ReservationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalVenues;
    private Long todayReservations;
    private Long totalUsers;
    private BigDecimal monthlyRevenue;
    private Long pendingReservations;
    private List<ReservationResponseDTO> recentReservations;
    private BigDecimal previousMonthRevenue;
    private Long currentPeriodReservations;
    private Long previousPeriodReservations;

    private List<MonthlyData> monthlyRevenue6Months;
    private List<MonthlyData> monthlyReservations6Months;
    private Map<String, Long> reservationsByStatus;
    private List<VenuePopularity> topVenues;
    private List<FurniturePopularity> topFurniture;
    private List<EventTypePopularity> topEventTypes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyData {
        private String month;
        private BigDecimal revenue;
        private Long reservations;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenuePopularity {
        private String venueName;
        private Long reservationCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FurniturePopularity {
        private String furnitureName;
        private Long requestCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventTypePopularity {
        private String eventTypeName;
        private Long reservationCount;
    }
}
