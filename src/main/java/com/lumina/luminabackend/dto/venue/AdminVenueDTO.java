package com.lumina.luminabackend.dto.venue;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminVenueDTO {
    private Integer venueId;
    private String venueName;
    private String address;
    private Integer districtId;
    private String districtName;
    private Integer maxCapacity;
    private BigDecimal pricePerHour;
    private String description;
    private String mainPhotoUrl;
    private String photos; // Comma separated URLs
    private String availableEventTypes; // Comma separated types
    private String status;
}