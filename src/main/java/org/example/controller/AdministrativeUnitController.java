package org.example.controller;

import org.example.api.AdministrativeUnitsApi;
import org.example.model.AdministrativeUnit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Implements the generated {@link AdministrativeUnitsApi} (tag "AdministrativeUnits").
 * All operations are stubs until the service layer is wired in.
 */
@RestController
public class AdministrativeUnitController implements AdministrativeUnitsApi {

    @Override
    public ResponseEntity<List<AdministrativeUnit>> listAdministrativeUnits() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<AdministrativeUnit> getAdministrativeUnitById(UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<Void> applyAdminUnitChangeset(MultipartFile file) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
