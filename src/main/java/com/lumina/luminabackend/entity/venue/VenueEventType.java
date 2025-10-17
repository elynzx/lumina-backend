package com.lumina.luminabackend.entity.venue;
import com.lumina.luminabackend.entity.event.EventType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "local_tipo_evento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(VenueEventTypeId.class)
public class VenueEventType {

    @Id
    @Column(name = "id_local")
    private Integer venueId;

    @Id
    @Column(name = "id_tipo_evento")
    private Integer eventTypeId;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local", insertable = false, updatable = false)
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_evento", insertable = false, updatable = false)
    private EventType eventType;
}