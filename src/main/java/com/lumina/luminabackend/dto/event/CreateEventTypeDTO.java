package com.lumina.luminabackend.dto.event;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventTypeDTO {

    @NotBlank(message = "El nombre del tipo de evento es obligatorio")
    @Size(max = 100, message = "El nombre del tipo de evento no puede exceder 100 caracteres")
    private String eventTypeName;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    @Size(max = 255, message = "La URL de foto no debe superar 255 caracteres")
    private String photoUrl;
}