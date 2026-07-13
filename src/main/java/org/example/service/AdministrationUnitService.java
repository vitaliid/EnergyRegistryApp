package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.components.UserActionPublisher;
import org.example.domain.AdministrationUnit;
import org.example.domain.AdministrationUnitType;
import org.example.domain.logging.UserActionType;
import org.example.dto.AdministrationUnitRequest;
import org.example.dto.AdministrationUnitResponse;
import org.example.dto.AdministrationUnitUpdateRequest;
import org.example.mappers.AdministrationUnitMapper;
import org.example.repository.AdministrationUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AdministrationUnitService {

    private final AdministrationUnitRepository administrationUnitRepository;
    private final AdministrationUnitMapper mapper;
    private final UserActionPublisher userActionPublisher;

    public List<AdministrationUnitResponse> getByType(AdministrationUnitType type) {
        return administrationUnitRepository.findByType(type)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(transactionManager = "transactionManager")
    public AdministrationUnitResponse create(AdministrationUnitRequest request) {
        try {
            AdministrationUnit entity = mapper.toEntity(request);
            if (request.getParentId() != null) {
                AdministrationUnit parent = administrationUnitRepository.findById(request.getParentId())
                        .orElseThrow(() -> new RuntimeException("Parent not found"));
                entity.setParent(parent);
            }
            AdministrationUnit saved = administrationUnitRepository.save(entity);
            userActionPublisher.success(
                    UserActionType.ADMINISTRATION_UNIT_CREATED,
                    "ADMINISTRATION_UNIT",
                    saved.getId(),
                    Map.of("type", saved.getType().name())
            );
            return mapper.toResponse(saved);
        } catch (Exception ex) {
            userActionPublisher.failure(
                    UserActionType.ADMINISTRATION_UNIT_CREATED,
                    "ADMINISTRATION_UNIT",
                    null,
                    "UPDATE_FAILED"
            );

            throw ex;
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public AdministrationUnitResponse update(Integer id,
                                             AdministrationUnitUpdateRequest request) {
        try {
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

            AdministrationUnit saved = administrationUnitRepository.save(unit);

            userActionPublisher.success(
                    UserActionType.ADMINISTRATION_UNIT_UPDATED,
                    "ADMINISTRATION_UNIT",
                    saved.getId(),
                    Map.of()
            );

            return mapper.toResponse(saved);
        } catch (RuntimeException ex) {
            userActionPublisher.failure(
                    UserActionType.ADMINISTRATION_UNIT_UPDATED,
                    "ADMINISTRATION_UNIT",
                    id,
                    "UPDATE_FAILED"
            );

            throw ex;
        }
    }
}
