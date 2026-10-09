package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.HomeAdministrativeAreasApi;
import org.example.model.HomeAdministrativeArea;
import org.example.service.HomeAdministrativeAreaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class HomeAdministrativeAreaController implements HomeAdministrativeAreasApi {

    private final HomeAdministrativeAreaService service;

    @Override
    public ResponseEntity<List<HomeAdministrativeArea>> listHomeAdministrativeAreas() {
        return ResponseEntity.ok(service.listAll());
    }

    @Override
    public ResponseEntity<HomeAdministrativeArea> getHomeAdministrativeAreaById(UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
