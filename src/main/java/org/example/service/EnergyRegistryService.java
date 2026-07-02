package org.example.service;

import org.example.components.EnergyMetrics;
import org.example.dto.EnergyProperties;
import org.example.dto.EnergyReadingRequest;
import org.example.dto.EnergyReadingResponse;
import org.springframework.stereotype.Service;

@Service
public class EnergyRegistryService {

    private final EnergyProperties energyProperties;
    private final EnergyMetrics energyMetrics;

    public EnergyRegistryService(
            EnergyProperties energyProperties,
            EnergyMetrics energyMetrics
    ) {
        this.energyProperties = energyProperties;
        this.energyMetrics = energyMetrics;
    }

    public EnergyReadingResponse registerReading(EnergyReadingRequest request) {
        boolean aboveThreshold = isAboveThreshold(request.value());

        energyMetrics.updateReading(request.meterId(), request.value());

        return new EnergyReadingResponse(
                request.meterId(),
                request.value(),
                energyProperties.getThreshold(),
                aboveThreshold
        );
    }

    public boolean isAboveThreshold(double value) {
        return value > energyProperties.getThreshold();
    }
}