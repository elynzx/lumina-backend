package com.lumina.luminabackend.entity.reservation;
import com.lumina.luminabackend.entity.event.EventType;
import com.lumina.luminabackend.entity.reservation.*;
import com.lumina.luminabackend.entity.user.User;
import com.lumina.luminabackend.entity.payment.Payment;
import com.lumina.luminabackend.entity.venue.Venue;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer reservationId;

    @Column(name = "fecha", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime startTime;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime endTime;

    @Column(name = "cantidad_personas", nullable = false)
    private Integer guestCount;

    @Column(name = "costo_local", nullable = false, precision = 10, scale = 2)
    private BigDecimal venueCost;

    @Column(name = "costo_mobiliario", precision = 10, scale = 2)
    private BigDecimal furnitureCost;

    @Column(name = "costo_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private ReservationStatus status;

    @Column(name = "fecha_reserva")
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local", nullable = false)
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_evento", nullable = false)
    private EventType eventType;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservationFurniture> furnitureItems;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;
}
