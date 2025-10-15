package com.lumina.luminabackend.entity.payment;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "metodos_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer paymentMethodId;

    @Column(name = "nombre_metodo", nullable = false, unique = true, length = 50)
    private String methodName;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    // Relationships
    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;
}