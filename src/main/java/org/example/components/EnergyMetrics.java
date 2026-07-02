package org.example.components;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class EnergyMetrics {

    private final MeterRegistry meterRegistry;
    private final Map<String, AtomicReference<Double>> meterValues = new ConcurrentHashMap<>();

    public EnergyMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void updateReading(String meterId, double value) {
        AtomicReference<Double> reading = meterValues.computeIfAbsent(meterId, this::registerGauge);
        reading.set(value);
    }

    public AtomicReference<Double> registerGauge(String meterId) {
        AtomicReference<Double> value = new AtomicReference<>(0.0);

        Gauge.builder("energy_meter_value", value, AtomicReference::get)
                .description("Latest energy meter reading")
                .tag("meter_id", meterId)
                .register(meterRegistry);

        return value;
    }
}