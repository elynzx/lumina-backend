package com.lumina.luminabackend.entity.venue;

import lombok.Getter;

@Getter
public enum VenueStatus {
    AVAILABLE("DISPONIBLE"),
    UNAVAILABLE("NO_DISPONIBLE");

    private final String value;

    VenueStatus(String value) {
        this.value = value;
    }

}