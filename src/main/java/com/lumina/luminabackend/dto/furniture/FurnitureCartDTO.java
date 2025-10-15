package com.lumina.luminabackend.dto.furniture;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureCartDTO {
    private Integer furnitureId;
    private String furnitureName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}