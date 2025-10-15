package com.lumina.luminabackend.dto.payment;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailDTO {
    private Integer paymentId;
    private BigDecimal amount;
    private String paymentMethod;
    private String confirmationCode;
    private String status; // PENDING, PAID, FAILED
    private LocalDateTime paymentDate;
    private String receiptUrl;
}