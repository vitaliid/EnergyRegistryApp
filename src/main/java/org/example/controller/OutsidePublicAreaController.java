package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.OutsidePublicAreasApi;
import org.example.model.OutsidePublicArea;
import org.example.model.OutsidePublicAreaCreate;
import org.example.model.OutsidePublicAreaUpdate;
import org.example.service.OutsidePublicAreaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OutsidePublicAreaController implements OutsidePublicAreasApi {

    private final OutsidePublicAreaService service;

    @Override
    public ResponseEntity<List<OutsidePublicArea>> listOutsidePublicAreas() {
        return ResponseEntity.ok(service.listAll());
    }

    @Override
    public ResponseEntity<OutsidePublicArea> getOutsidePublicAreaById(UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<OutsidePublicArea> createOutsidePublicArea(OutsidePublicAreaCreate outsidePublicAreaCreate) {
        OutsidePublicArea created = service.create(outsidePublicAreaCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<OutsidePublicArea> patchOutsidePublicArea(UUID id, OutsidePublicAreaUpdate outsidePublicAreaUpdate) {
        return ResponseEntity.ok(service.patch(id, outsidePublicAreaUpdate));
    }

    @Override
    public ResponseEntity<Void> deleteOutsidePublicArea(UUID id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
