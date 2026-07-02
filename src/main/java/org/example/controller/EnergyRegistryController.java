package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.EnergyReadingRequest;
import org.example.dto.EnergyReadingResponse;
import org.example.service.EnergyRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnergyRegistryController {

    private final EnergyRegistryService energyRegistryService;

    @PostMapping("/readings")
    public ResponseEntity<EnergyReadingResponse> addReading(
            @RequestBody EnergyReadingRequest request
    ) {
        return ResponseEntity
                .accepted()
                .body(energyRegistryService.registerReading(request));
    }
}
