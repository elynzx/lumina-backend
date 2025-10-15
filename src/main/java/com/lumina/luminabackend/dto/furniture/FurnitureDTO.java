package com.lumina.luminabackend.dto.furniture;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureDTO {
    private Integer furnitureId;
    private String furnitureName;
    private String description;
    private Integer totalStock;
    private BigDecimal unitPrice;
    private String photoUrl;
}