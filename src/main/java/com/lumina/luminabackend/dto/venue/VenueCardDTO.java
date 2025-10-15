package com.lumina.luminabackend.dto.venue;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueCardDTO {
    private Integer venueId;
    private String venueName;
    private String address;
    private String districtName;
    private Integer maxCapacity;
    private BigDecimal pricePerHour;
    private String description;
    private String mainPhotoUrl;
    private String status; // AVAILABLE, UNAVAILABLE
}