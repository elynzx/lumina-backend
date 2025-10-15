package com.lumina.luminabackend.dto.reservation;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItemDTO {
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}