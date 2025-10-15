package com.lumina.luminabackend.dto.furniture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFurnitureDTO {

    @Size(max = 100, message = "Máximo 100 caracteres")
    private String furnitureName;

    @Size(max = 500, message = "Máximo 500 caracteres")
    private String description;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer totalStock;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal unitPrice;

    @Size(max = 255, message = "La URL de foto no debe superar 255 caracteres")
    private String photoUrl;
}