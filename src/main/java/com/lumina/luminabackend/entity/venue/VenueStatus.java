package com.lumina.luminabackend.entity.venue;

public enum VenueStatus {
    AVAILABLE("DISPONIBLE"),
    UNAVAILABLE("NO_DISPONIBLE");

    private final String value;

    VenueStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}