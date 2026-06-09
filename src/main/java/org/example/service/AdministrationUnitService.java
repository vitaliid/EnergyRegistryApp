package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnitType;
import org.example.dto.AdministrationUnitResponse;
import org.example.mappers.AdministrationUnitMapper;
import org.example.repository.AdministrationUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdministrationUnitService {

    private final AdministrationUnitRepository administrationUnitRepository;
    private final AdministrationUnitMapper mapper;

    public List<AdministrationUnitResponse> getByType(AdministrationUnitType type) {
        return administrationUnitRepository.findByType(type)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}
