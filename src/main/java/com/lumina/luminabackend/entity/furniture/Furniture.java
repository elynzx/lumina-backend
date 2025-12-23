package com.lumina.luminabackend.entity.furniture;

import com.lumina.luminabackend.entity.reservation.ReservationFurniture;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "mobiliario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Furniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mobiliario")
    private Integer furnitureId;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String furnitureName;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "stock_total", nullable = false)
    private Integer totalStock;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "url_foto", nullable = false, length = 255)
    private String photoUrl;

    // Relationships
    @OneToMany(mappedBy = "furniture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservationFurniture> reservationFurniture;
}