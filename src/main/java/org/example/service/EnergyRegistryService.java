package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.components.EnergyMetrics;
import org.example.components.UserActionKafkaPublisher;
import org.example.domain.logging.UserActionType;
import org.example.dto.EnergyProperties;
import org.example.dto.EnergyReadingRequest;
import org.example.dto.EnergyReadingResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnergyRegistryService {

    private final EnergyProperties energyProperties;
    private final EnergyMetrics energyMetrics;
    private final UserActionKafkaPublisher userActionKafkaPublisher;

    @Transactional(transactionManager = "transactionManager")
    public EnergyReadingResponse registerReading(EnergyReadingRequest request) {
        try {
            boolean aboveThreshold = isAboveThreshold(request.value());

            energyMetrics.updateReading(request.meterId(), request.value());

            EnergyReadingResponse response = new EnergyReadingResponse(
                    request.meterId(),
                    request.value(),
                    energyProperties.getThreshold(),
                    aboveThreshold
            );

            userActionKafkaPublisher.success(
                    UserActionType.ENERGY_READING_SUBMITTED,
                    "ENERGY_METER",
                    request.meterId(),
                    Map.of(
                            "aboveThreshold", aboveThreshold,
                            "threshold", energyProperties.getThreshold()
                    )
            );

            return response;

        } catch (IllegalArgumentException ex) {
            userActionKafkaPublisher.failure(
                    UserActionType.ENERGY_READING_SUBMITTED,
                    "ENERGY_METER",
                    request.meterId(),
                    "INVALID_READING"
            );
            throw ex;

        } catch (RuntimeException ex) {
            userActionKafkaPublisher.failure(
                    UserActionType.ENERGY_READING_SUBMITTED,
                    "ENERGY_METER",
                    request.meterId(),
                    "PROCESSING_FAILED"
            );
            throw ex;
        }
    }

    public boolean isAboveThreshold(double value) {
        return value > energyProperties.getThreshold();
    }
}