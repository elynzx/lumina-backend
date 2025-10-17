package com.lumina.luminabackend.entity.venue;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotos_locales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenuePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer photoId;

    @Column(name = "url_foto", nullable = false, length = 255)
    private String photoUrl;

    @Column(name = "descripcion", length = 255)
    private String description;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local", nullable = false)
    private Venue venue;
}