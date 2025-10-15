package com.lumina.luminabackend.entity.payment;
import com.lumina.luminabackend.entity.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer paymentId;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private PaymentStatus status;

    @Column(name = "codigo_confirmacion", length = 50)
    private String confirmationCode;

    @Column(name = "fecha_pago")
    private LocalDateTime paymentDate;

    @Column(name = "comprobante_url", length = 255)
    private String receiptUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private PaymentMethod paymentMethod;
}

