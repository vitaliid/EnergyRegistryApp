package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnit;
import org.example.domain.AdministrationUnitAttribute;
import org.example.dto.AdministrationUnitAttributeRequest;
import org.example.dto.AdministrationUnitAttributeResponse;
import org.example.mappers.AdministrationUnitAttributeMapper;
import org.example.repository.AdministrationUnitAttributeRepository;
import org.example.repository.AdministrationUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministrationUnitAttributeService {

    private final AdministrationUnitAttributeRepository repository;
    private final AdministrationUnitRepository unitRepository;
    private final AdministrationUnitAttributeMapper mapper;

    public List<AdministrationUnitAttributeResponse> getByUnitId(Long unitId) {
        return repository.findByUnitId(unitId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public AdministrationUnitAttributeResponse create(Integer unitId,
                                                      AdministrationUnitAttributeRequest request) {

        AdministrationUnit unit = unitRepository
                .findById(unitId)
                .orElseThrow(() -> new EntityNotFoundException("AdministrationUnit not found"));

        AdministrationUnitAttribute attribute = mapper.toEntity(request);

        attribute.setUnit(unit);

        return mapper.toResponse(
                repository.save(attribute)
        );
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}