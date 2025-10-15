package com.lumina.luminabackend.dto.furniture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminFurnitureDTO {
    private Integer furnitureId;
    private String furnitureName;
    private String description;
    private Integer totalStock;
    private BigDecimal unitPrice;
    private String photoUrl;
    private LocalDateTime createdAt;
    private Integer totalReservations;
}