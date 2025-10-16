package com.lumina.luminabackend.dto.payment;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private Integer paymentMethodId;
    private String methodName;
    private String description;
    private Boolean active;
}