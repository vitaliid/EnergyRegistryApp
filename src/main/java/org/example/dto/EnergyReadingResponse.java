package org.example.dto;

public record EnergyReadingResponse(
        String meterId,
        double value,
        double threshold,
        boolean aboveThreshold
) {
}