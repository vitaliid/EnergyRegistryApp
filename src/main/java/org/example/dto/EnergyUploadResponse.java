package org.example.dto;

import java.util.List;

public record EnergyUploadResponse(
        int filesProcessed,
        int readingsProcessed,
        List<EnergyReadingResponse> readings
) {
}