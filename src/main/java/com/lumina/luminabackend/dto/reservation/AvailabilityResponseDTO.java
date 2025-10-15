package com.lumina.luminabackend.dto.reservation;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponseDTO {
    private Boolean isAvailable;
    private String message;
    private List<String> conflictingReservations;
}
