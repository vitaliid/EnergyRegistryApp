package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AdministrationUnitAttributeRequest;
import org.example.dto.AdministrationUnitAttributeResponse;
import org.example.service.AdministrationUnitAttributeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administration-units")
@RequiredArgsConstructor
public class AdministrationUnitAttributeController {

    private final AdministrationUnitAttributeService service;

    @GetMapping("/{unitId}/attributes")
    public List<AdministrationUnitAttributeResponse> getAttributes(
            @PathVariable Long unitId) {

        return service.getByUnitId(unitId);
    }

    @PostMapping("/{unitId}/attributes")
    public AdministrationUnitAttributeResponse createAttribute(
            @PathVariable("unitId") Integer unitId,
            @RequestBody
            AdministrationUnitAttributeRequest request) {

        return service.create(unitId, request);
    }

    @DeleteMapping("/attributes/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
