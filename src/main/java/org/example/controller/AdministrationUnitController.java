package org.example.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnitType;
import org.example.dto.AdministrationUnitResponse;
import org.example.service.AdministrationUnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/administration-units")
@RequiredArgsConstructor
public class AdministrationUnitController {

    private final AdministrationUnitService service;

    @GetMapping("/{type}")
    public List<AdministrationUnitResponse> getByType(
            @PathVariable("type")// it should be specified for correct swagger
            @Parameter(
                    description = "Type of unit",
                    schema = @Schema(implementation = AdministrationUnitType.class)
            )
            AdministrationUnitType type) {

        return service.getByType(type);
    }
}
