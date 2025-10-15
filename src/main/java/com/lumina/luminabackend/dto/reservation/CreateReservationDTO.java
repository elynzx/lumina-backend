package com.lumina.luminabackend.dto.reservation;
import com.lumina.luminabackend.dto.furniture.FurnitureCartDTO;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationDTO {
    @NotNull(message = "Debe seleccionar un local")
    private Integer venueId;

    @NotNull(message = "Debe seleccionar un tipo de evento")
    private Integer eventTypeId;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha debe ser hoy o futura")
    private LocalDate reservationDate;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime startTime;

    @NotNull(message = "La cantidad de personas es obligatoria")
    private LocalTime endTime;

    @Min(value = 1, message = "Debe haber al menos 1 persona")
    private Integer guestCount;

    private List<FurnitureCartDTO> furnitureItems;
}