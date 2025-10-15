package com.lumina.luminabackend.dto.venue;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueServiceDTO {
    private String serviceName;
    private String description;
    private String iconUrl;
}