package org.example.dto;

public record EnergyReadingRequest(
        String meterId,
        double value
) {
}
