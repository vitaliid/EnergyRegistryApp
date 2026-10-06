package org.example.controller;

import org.example.api.EsrApi;
import org.example.model.AbstractBuildingConsumption;
import org.example.model.AbstractBuildingConsumptionInput;
import org.example.model.BuildingConsumptionCategory;
import org.example.model.EnergyCarrierUnit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Implements the generated {@link EsrApi} (tag "ESR" – Energy Supply Register).
 * Covers the building consumption entries of a public entity and the ESR master data
 * (building consumption categories, energy carrier units).
 * All operations are stubs until the service layer is wired in.
 */
@RestController
public class EsrController implements EsrApi {

    // --- building consumption entries of a public entity ---

    @Override
    public ResponseEntity<List<AbstractBuildingConsumption>> getBuildingEsrEntries(UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<AbstractBuildingConsumption> createBuildingEsrEntry(
            UUID id,
            AbstractBuildingConsumptionInput abstractBuildingConsumptionInput) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<AbstractBuildingConsumption> replaceBuildingEsrEntry(
            UUID publicEntityId,
            UUID id,
            AbstractBuildingConsumptionInput abstractBuildingConsumptionInput) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<Void> deleteBuildingEsrEntry(UUID publicEntityId, UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    // --- master data ---

    @Override
    public ResponseEntity<List<BuildingConsumptionCategory>> listBuildingConsumptionCategories() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<List<EnergyCarrierUnit>> listEnergyCarrierUnits() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
