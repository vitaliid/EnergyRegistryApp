package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.components.EnergyMetrics;
import org.example.components.UserActionPublisher;
import org.example.domain.logging.UserActionType;
import org.example.dto.EnergyProperties;
import org.example.dto.EnergyReadingRequest;
import org.example.dto.EnergyReadingResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnergyRegistryService {

    private final EnergyProperties energyProperties;
    private final EnergyMetrics energyMetrics;
    private final UserActionPublisher userActionPublisher;

    public EnergyReadingResponse registerReading(EnergyReadingRequest request) {
        boolean aboveThreshold = isAboveThreshold(request.value());

        energyMetrics.updateReading(request.meterId(), request.value());

        EnergyReadingResponse response = new EnergyReadingResponse(
                request.meterId(),
                request.value(),
                energyProperties.getThreshold(),
                aboveThreshold
        );

        userActionPublisher.success(
                UserActionType.ENERGY_READING_SUBMITTED,
                "ENERGY_READING",
                request.meterId(),
                Map.of("value", request.value(), "aboveThreshold", aboveThreshold)
        );

        return response;
    }

    public boolean isAboveThreshold(double value) {
        return value > energyProperties.getThreshold();
    }
}