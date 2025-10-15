package com.lumina.luminabackend.dto.district;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDistrictDTO {

    @NotBlank(message = "El nombre del distrito es obligatorio")
    @Size(max = 100, message = "El nombre del distrito no puede exceder 100 caracteres")
    private String districtName;
}