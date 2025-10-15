package com.lumina.luminabackend.dto.event;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeDTO {
    private Integer eventTypeId;
    private String eventTypeName;
    private String description;
}