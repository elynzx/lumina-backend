package com.lumina.luminabackend.entity.district;
import com.lumina.luminabackend.entity.venue.Venue;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "distritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distrito")
    private Integer districtId;

    @Column(name = "nombre_distrito", nullable = false, unique = true, length = 100)
    private String districtName;

    // Relationships
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venue> venues;
}
