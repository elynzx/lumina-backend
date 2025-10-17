package com.lumina.luminabackend.dto.payment;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotNull(message = "El ID de la reserva es obligatorio")
    private Integer reservationId;

    @NotNull(message = "El método de pago es obligatorio")
    private Integer paymentMethodId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    private String transactionCode;
    private String customerName;
    private String customerEmail;
}
