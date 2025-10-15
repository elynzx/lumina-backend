package com.lumina.luminabackend.dto.venue;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueSliderDTO {
    private Integer venueId;
    private String venueName;
    private String mainPhoto;
}