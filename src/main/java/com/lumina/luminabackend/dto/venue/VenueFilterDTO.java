package com.lumina.luminabackend.dto.venue;

import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueFilterDTO {
    private Integer districtId;
    private Integer eventTypeId;
    private Integer minCapacity;
    private Integer maxCapacity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}