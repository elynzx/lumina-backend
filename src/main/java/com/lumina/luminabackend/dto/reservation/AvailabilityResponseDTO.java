package com.lumina.luminabackend.dto.reservation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponseDTO {
    private Boolean isAvailable;
    private String message;
    private List<ConflictInfo> conflicts;

    @Data
    @Builder
    public static class ConflictInfo {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String eventType;
    }
}