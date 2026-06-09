package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnit;
import org.example.domain.AdministrationUnitType;
import org.example.dto.AdministrationUnitRequest;
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

    @Transactional
    public AdministrationUnitResponse create(AdministrationUnitRequest request) {

        AdministrationUnit entity = mapper.toEntity(request);

        // handle parent relation
        if (request.getParentId() != null) {
            AdministrationUnit parent = administrationUnitRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            entity.setParent(parent);
        }

        AdministrationUnit saved = administrationUnitRepository.save(entity);

        return mapper.toResponse(saved);
    }
}
