package com.lumina.luminabackend.entity.reservation;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    PENDING("Pendiente"),
    CONFIRMED("Confirmada"),
    CANCELLED("Cancelada");

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

}