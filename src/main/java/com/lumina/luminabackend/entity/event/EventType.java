package com.lumina.luminabackend.entity.event;
import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.venue.VenueEventType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tipos_evento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_evento")
    private Integer eventTypeId;

    @Column(name = "nombre_tipo", nullable = false, unique = true, length = 100)
    private String eventTypeName;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url_foto", length = 255)
    private String photoUrl;

    // Relationships
    @OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VenueEventType> venueEventTypes;

    @OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}