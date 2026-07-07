package org.example.controller;

import org.example.service.AdministrationUnitSearchService;
import lombok.RequiredArgsConstructor;
import org.example.dto.AdministrationUnitSearchDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/administration-units")
@RequiredArgsConstructor
public class AdministrationUnitSearchController {

    private final AdministrationUnitSearchService searchService;

    @GetMapping("/search")
    public List<AdministrationUnitSearchDto> search(
            @RequestParam("query") String query,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        return searchService.search(query, limit);
    }
}