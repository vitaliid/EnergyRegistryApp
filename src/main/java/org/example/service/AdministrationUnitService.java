package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnit;
import org.example.domain.AdministrationUnitType;
import org.example.dto.AdministrationUnitRequest;
import org.example.dto.AdministrationUnitResponse;
import org.example.dto.AdministrationUnitUpdateRequest;
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

    @Transactional
    public AdministrationUnitResponse create(AdministrationUnitRequest request) {

        AdministrationUnit entity = mapper.toEntity(request);

        if (request.getParentId() != null) {
            AdministrationUnit parent = administrationUnitRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            entity.setParent(parent);
        }

        AdministrationUnit saved = administrationUnitRepository.save(entity);

        return mapper.toResponse(saved);
    }

    @Transactional
    public AdministrationUnitResponse update(Integer id,
                                             AdministrationUnitUpdateRequest request) {

        AdministrationUnit unit = administrationUnitRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "AdministrationUnit not found: " + id));

        if (request.getType() != null) {
            unit.setType(request.getType());
        }

        if (request.getUnitName() != null) {
            unit.setUnitName(request.getUnitName());
        }

        if (request.getParentId() != null) {
            AdministrationUnit parent = administrationUnitRepository.findById(request.getParentId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Parent not found: " + request.getParentId()));

            unit.setParent(parent);
        } else {
            unit.setParent(null);
        }

        return mapper.toResponse(administrationUnitRepository.save(unit));
    }
}
