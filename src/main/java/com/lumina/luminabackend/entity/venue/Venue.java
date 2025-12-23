package com.lumina.luminabackend.entity.venue;
import com.lumina.luminabackend.entity.district.District;
import com.lumina.luminabackend.entity.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "locales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
    private Integer venueId;

    @Column(name = "nombre_local", nullable = false, unique = true, length = 150)
    private String venueName;

    @Column(name = "direccion", nullable = false, length = 255)
    private String address;

    @Column(name = "aforo_maximo", nullable = false)
    private Integer maxCapacity;

    @Column(name = "precio_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private VenueStatus status;

    @Column(name = "latitud")
    private Double latitude;

    @Column(name = "longitud")
    private Double longitude;

    @Column(name = "url_google_maps", length = 500)
    private String googleMapsUrl;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_distrito", nullable = false)
    private District district;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VenuePhoto> photos;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VenueEventType> venueEventTypes;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}