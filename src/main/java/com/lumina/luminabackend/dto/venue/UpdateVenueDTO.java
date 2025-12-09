package com.lumina.luminabackend.dto.venue;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVenueDTO {

    @Size(max = 150, message = "El nombre del local no puede exceder 150 caracteres")
    private String venueName;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String address;

    private Integer districtId;

    @Min(value = 1, message = "La capacidad máxima debe ser mayor a 0")
    private Integer maxCapacity;

    @DecimalMin(value = "0.01", message = "El precio por hora debe ser mayor a 0")
    private BigDecimal pricePerHour;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    private String mainPhotoUrl;
    private String photos;
    private String availableEventTypes;

    @Pattern(regexp = "AVAILABLE|UNAVAILABLE", message = "El estado debe ser AVAILABLE o UNAVAILABLE")
    private String status;

    private Double latitude;
    private Double longitude;
    private String googleMapsUrl;
}