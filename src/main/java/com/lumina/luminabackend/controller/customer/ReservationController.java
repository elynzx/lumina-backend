package com.lumina.luminabackend.controller.customer;

import com.lumina.luminabackend.dto.reservation.*;
import com.lumina.luminabackend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/availability")
    public ResponseEntity<?> checkAvailability(@RequestBody AvailabilityRequestDTO request) {
        try {
            boolean isAvailable = reservationService.checkAvailability(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of("available", isAvailable),
                    "message", isAvailable ? "Fecha disponible" : "Fecha no disponible"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error al verificar disponibilidad: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/calculate-budget")
    public ResponseEntity<?> calculateBudget(@RequestBody BudgetCalculationDTO request) {
        try {
            BudgetCalculationDTO budget = reservationService.calculateBudget(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", budget,
                    "message", "Presupuesto calculado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error al calcular presupuesto: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/{reservationId}/details")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> getReservationDetails(@PathVariable Integer reservationId) {
        try {
            ReservationSuccessDTO reservation = reservationService.getReservationDetails(reservationId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", reservation,
                    "message", "Detalles de reserva obtenidos correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error al obtener detalles: " + e.getMessage()
            ));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDTO request) {
        try {
            ReservationResponseDTO reservation = reservationService.createReservation(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", reservation,
                    "message", "Reserva creada exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error al crear reserva: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/my-reservations")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> getMyReservations() {
        try {
            List<ReservationResponseDTO> reservations = reservationService.getMyReservations();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", reservations,
                    "message", "Reservas obtenidas correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error al obtener reservas: " + e.getMessage()
            ));
        }
    }
}