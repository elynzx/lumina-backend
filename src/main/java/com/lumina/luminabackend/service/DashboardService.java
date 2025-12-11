package com.lumina.luminabackend.service;

import com.lumina.luminabackend.dto.dashboard.DashboardStatsDTO;
import com.lumina.luminabackend.dto.reservation.ReservationResponseDTO;
import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import com.lumina.luminabackend.repository.reservation.ReservationRepository;
import com.lumina.luminabackend.repository.user.UserRepository;
import com.lumina.luminabackend.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final VenueRepository venueRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public DashboardStatsDTO getDashboardStats(String period) {
        // Determinar el rango de fechas según el período
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period != null ? period : "month") {
            case "week":
                startDate = endDate.minusWeeks(1);
                break;
            case "year":
                startDate = endDate.minusYears(1);
                break;
            case "month":
            default:
                startDate = endDate.minusMonths(1);
                break;
        }

        // Total de locales (no cambia)
        long totalVenues = venueRepository.count();

        // Reservas de hoy (no cambia)
        LocalDate today = LocalDate.now();
        long todayReservations = reservationRepository.countByReservationDate(today);

        // Total de usuarios (no cambia)
        long totalUsers = userRepository.count();

        // Ingresos del período actual
        BigDecimal currentRevenue = reservationRepository.sumTotalAmountByCreatedAtRangeAndStatus(
                startDate, endDate, ReservationStatus.CONFIRMED
        );

        // Ingresos del período anterior (para calcular crecimiento)
        LocalDate previousStartDate;
        LocalDate previousEndDate = startDate.minusDays(1);

        switch (period != null ? period : "month") {
            case "week":
                previousStartDate = previousEndDate.minusWeeks(1);
                break;
            case "year":
                previousStartDate = previousEndDate.minusYears(1);
                break;
            case "month":
            default:
                previousStartDate = previousEndDate.minusMonths(1);
                break;
        }

        BigDecimal previousRevenue = reservationRepository.sumTotalAmountByCreatedAtRangeAndStatus(
                previousStartDate, previousEndDate, ReservationStatus.CONFIRMED
        );

        // Reservas del período actual
        long currentReservations = reservationRepository.countByCreatedAtRange(startDate, endDate);

        // Reservas del período anterior
        long previousReservations = reservationRepository.countByCreatedAtRange(previousStartDate, previousEndDate);

        // Reservas pendientes (no cambia)
        long pendingReservations = reservationRepository.countByStatus(ReservationStatus.PENDING);

        // Últimas 5 reservas (no cambia)
        List<Reservation> recentReservationsList = reservationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        List<ReservationResponseDTO> recentReservations = recentReservationsList.stream()
                .map(this::mapToSimpleDTO)
                .collect(Collectors.toList());

        // Datos para gráficos (ajustar según período)
        List<DashboardStatsDTO.MonthlyData> monthlyData = getMonthlyData(period);
        Map<String, Long> reservationsByStatus = getReservationsByStatus();
        List<DashboardStatsDTO.VenuePopularity> topVenues = getTopVenues();
        List<DashboardStatsDTO.FurniturePopularity> topFurniture = getTopFurniture();
        List<DashboardStatsDTO.EventTypePopularity> topEventTypes = getTopEventTypes();

        return DashboardStatsDTO.builder()
                .totalVenues(totalVenues)
                .todayReservations(todayReservations)
                .totalUsers(totalUsers)
                .monthlyRevenue(currentRevenue != null ? currentRevenue : BigDecimal.ZERO)
                .previousMonthRevenue(previousRevenue != null ? previousRevenue : BigDecimal.ZERO)
                .currentPeriodReservations(currentReservations)
                .previousPeriodReservations(previousReservations)
                .pendingReservations(pendingReservations)
                .recentReservations(recentReservations)
                .monthlyRevenue6Months(monthlyData)
                .monthlyReservations6Months(monthlyData)
                .reservationsByStatus(reservationsByStatus)
                .topVenues(topVenues)
                .topFurniture(topFurniture)
                .topEventTypes(topEventTypes)
                .build();
    }

    private List<DashboardStatsDTO.MonthlyData> getMonthlyData(String period) {
        List<DashboardStatsDTO.MonthlyData> monthlyData = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();

        int periods = "year".equals(period) ? 12 : 6;

        for (int i = periods - 1; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);

            BigDecimal revenue = reservationRepository.sumTotalAmountByYearAndMonthAndStatus(
                    month.getYear(),
                    month.getMonthValue(),
                    ReservationStatus.CONFIRMED
            );

            long reservations = reservationRepository.countByYearAndMonth(
                    month.getYear(),
                    month.getMonthValue()
            );

            monthlyData.add(DashboardStatsDTO.MonthlyData.builder()
                    .month(month.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "PE")))
                    .revenue(revenue != null ? revenue : BigDecimal.ZERO)
                    .reservations(reservations)
                    .build());
        }

        return monthlyData;
    }

    private Map<String, Long> getReservationsByStatus() {
        Map<String, Long> statusMap = new HashMap<>();
        statusMap.put("CONFIRMED", reservationRepository.countByStatus(ReservationStatus.CONFIRMED));
        statusMap.put("PENDING", reservationRepository.countByStatus(ReservationStatus.PENDING));
        statusMap.put("CANCELLED", reservationRepository.countByStatus(ReservationStatus.CANCELLED));
        return statusMap;
    }

    private List<DashboardStatsDTO.VenuePopularity> getTopVenues() {
        List<Object[]> results = reservationRepository.findTopVenuesByReservationCount();
        return results.stream()
                .map(result -> DashboardStatsDTO.VenuePopularity.builder()
                        .venueName((String) result[0])
                        .reservationCount((Long) result[1])
                        .build())
                .collect(Collectors.toList());
    }

    private List<DashboardStatsDTO.FurniturePopularity> getTopFurniture() {
        List<Object[]> results = reservationRepository.findTopFurnitureByRequestCount();
        return results.stream()
                .limit(10)
                .map(result -> DashboardStatsDTO.FurniturePopularity.builder()
                        .furnitureName((String) result[0])
                        .requestCount((Long) result[1])
                        .build())
                .collect(Collectors.toList());
    }

    private List<DashboardStatsDTO.EventTypePopularity> getTopEventTypes() {
        List<Object[]> results = reservationRepository.findTopEventTypesByReservationCount();
        return results.stream()
                .map(result -> DashboardStatsDTO.EventTypePopularity.builder()
                        .eventTypeName((String) result[0])
                        .reservationCount((Long) result[1])
                        .build())
                .collect(Collectors.toList());
    }

    private ReservationResponseDTO mapToSimpleDTO(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .reservationId(reservation.getReservationId())
                .venueName(reservation.getVenue().getVenueName())
                .customerName(reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName())
                .reservationDate(reservation.getReservationDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .totalCost(reservation.getTotalCost())
                .status(reservation.getStatus())
                .build();
    }
}
