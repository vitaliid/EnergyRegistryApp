package org.example.controller;

import org.example.api.PublicEntitiesApi;
import org.example.model.PublicEntity;
import org.example.model.PublicEntityCreate;
import org.example.model.PublicEntityUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Implements the generated {@link PublicEntitiesApi} (tag "PublicEntities").
 * All operations are stubs until the service layer is wired in.
 */
@RestController
public class PublicEntityController implements PublicEntitiesApi {

    @Override
    public ResponseEntity<List<PublicEntity>> listPublicEntities() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<PublicEntity> createPublicEntity(PublicEntityCreate publicEntityCreate) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<PublicEntity> getPublicEntityById(UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<PublicEntity> patchPublicEntity(UUID id, PublicEntityUpdate publicEntityUpdate) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<Void> deletePublicEntity(UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
