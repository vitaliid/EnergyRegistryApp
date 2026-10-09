package org.example.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;

/**
 * Building consumption that is not exclusively for heat generation.
 */
@Entity
@Audited
@DiscriminatorValue(BuildingConsumptionEntity.DISCRIMINATOR)
public class BuildingConsumptionEntity extends EsrConsumptionEntity {

    public static final String DISCRIMINATOR = "BuildingConsumption";
}
