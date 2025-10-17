package com.lumina.luminabackend.dto.venue;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenuePreviewDTO {
    private Integer venueId;
    private String venueName;
    private String districtName;
    private String description;
    private String mainPhoto;
    private BigDecimal pricePerHour;
}