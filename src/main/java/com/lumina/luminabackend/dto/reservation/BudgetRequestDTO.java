package com.lumina.luminabackend.dto.reservation;
import com.lumina.luminabackend.dto.furniture.FurnitureCartDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequestDTO {
    @NotNull
    Integer venueId;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private List<FurnitureCartDTO> furnitureItems;
}