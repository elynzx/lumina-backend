package com.lumina.luminabackend.entity.reservation;

public enum ReservationStatus {
    PENDING("Pendiente"),
    CONFIRMED("Confirmada"),
    CANCELLED("Cancelada");

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}