package com.lumina.luminabackend.dto.reservation;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponseDTO {
    private BigDecimal venueCost;
    private BigDecimal furnitureCost;
    private BigDecimal totalCost;
    private Integer totalHours;
    private List<BudgetItemDTO> breakdown;
}