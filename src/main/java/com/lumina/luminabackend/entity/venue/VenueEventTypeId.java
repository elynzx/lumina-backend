package com.lumina.luminabackend.entity.venue;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueEventTypeId implements Serializable {
    private Integer venueId;
    private Integer eventTypeId;
}