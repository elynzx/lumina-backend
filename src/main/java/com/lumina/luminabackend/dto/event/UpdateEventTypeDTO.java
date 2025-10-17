package com.lumina.luminabackend.dto.event;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventTypeDTO {

    @Size(max = 100, message = "El nombre del tipo de evento no puede exceder 100 caracteres")
    private String eventTypeName;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;
}