package com.lumina.luminabackend.dto.venue;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDetailDTO {
    private Integer venueId;
    private String venueName;
    private String address;
    private String districtName;
    private Integer maxCapacity;
    private BigDecimal pricePerHour;
    private String fullDescription;
    private List<String> photos;
    private List<String> availableEventTypes;
    private List<VenueServiceDTO> services;
    private String status;
}
