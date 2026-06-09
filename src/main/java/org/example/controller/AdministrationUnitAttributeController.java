package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnitAttribute;
import org.example.service.AdministrationUnitAttributeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administration-units")
@RequiredArgsConstructor
public class AdministrationUnitAttributeController {

    private final AdministrationUnitAttributeService service;

    // GET attributes for unit
    @GetMapping("/{unitId}/attributes")
    public List<AdministrationUnitAttribute> getAttributes(
            @PathVariable Long unitId) {

        return service.getByUnitId(unitId);
    }

    // CREATE attribute
    @PostMapping("/{unitId}/attributes")
    public AdministrationUnitAttribute createAttribute(
            @PathVariable Integer unitId,
            @RequestParam String key,
            @RequestParam String value) {

        return service.create(unitId, key, value);
    }

    // DELETE attribute
    @DeleteMapping("/attributes/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
