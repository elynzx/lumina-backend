package com.lumina.luminabackend.controller.admin;

import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.reservation.ReservationResponseDTO;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import com.lumina.luminabackend.service.AdminReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reservations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ReservationResponseDTO>>> getAllReservations() {
        List<ReservationResponseDTO> reservations = adminReservationService.getAllReservations();
        return ResponseEntity.ok(ApiResponseDTO.success("Reservas obtenidas exitosamente", reservations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ReservationResponseDTO>> getReservationById(@PathVariable Integer id) {
        ReservationResponseDTO reservation = adminReservationService.getReservationById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Reserva obtenida exitosamente", reservation));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseDTO<ReservationResponseDTO>> updateReservationStatus(
            @PathVariable Integer id,
            @RequestParam ReservationStatus status) {
        ReservationResponseDTO reservation = adminReservationService.updateReservationStatus(id, status);
        return ResponseEntity.ok(ApiResponseDTO.success("Estado de reserva actualizado exitosamente", reservation));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<ReservationResponseDTO>>> searchReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer venueId,
            @RequestParam(required = false) Integer userId) {
        List<ReservationResponseDTO> reservations = adminReservationService.searchReservations(
                status, startDate, endDate, venueId, userId);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada exitosamente", reservations));
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<ApiResponseDTO<List<ReservationResponseDTO>>> getReservationsByStatus(
            @PathVariable ReservationStatus status) {
        List<ReservationResponseDTO> reservations = adminReservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(ApiResponseDTO.success("Reservas obtenidas exitosamente", reservations));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponseDTO<Object>> getReservationStatistics() {
        Object statistics = adminReservationService.getReservationStatistics();
        return ResponseEntity.ok(ApiResponseDTO.success("Estadísticas obtenidas exitosamente", statistics));
    }
}
