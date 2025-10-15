package com.lumina.luminabackend.entity.payment;

public enum PaymentStatus {
    PENDING("Pendiente"),
    PAID("Pagado"),
    FAILED("Fallido");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
