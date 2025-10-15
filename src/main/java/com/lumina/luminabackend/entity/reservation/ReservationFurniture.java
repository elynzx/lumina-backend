package com.lumina.luminabackend.entity.reservation;
import com.lumina.luminabackend.entity.furniture.Furniture;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "reserva_mobiliario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ReservationFurnitureId.class)
public class ReservationFurniture {

    @Id
    @Column(name = "id_reserva")
    private Integer reservationId;

    @Id
    @Column(name = "id_mobiliario")
    private Integer furnitureId;

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", insertable = false, updatable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mobiliario", insertable = false, updatable = false)
    private Furniture furniture;
}