package com.lumina.luminabackend.entity.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("Pendiente"),
    PAID("Pagado"),
    FAILED("Fallido");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

}
