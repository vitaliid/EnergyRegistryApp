package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * Building consumption exclusively for heat generation.
 */
@Getter
@Setter
@Entity
@Audited
@DiscriminatorValue(HeatBuildingConsumptionEntity.DISCRIMINATOR)
public class HeatBuildingConsumptionEntity extends EsrConsumptionEntity {

    public static final String DISCRIMINATOR = "HeatBuildingConsumption";

    /**
     * Whether the consumption is weather-adjusted with DWD climate factors.
     */
    @Column(name = "is_weather_adjusted", nullable = false)
    private Boolean isWeatherAdjusted;
}
