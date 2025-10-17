package com.lumina.luminabackend.dto.event;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEventTypeDTO {
    private Integer eventTypeId;
    private String eventTypeName;
    private String description;
}
