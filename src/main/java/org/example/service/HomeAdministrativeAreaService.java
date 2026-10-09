package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.mapper.HomeAdministrativeAreaMapper;
import org.example.model.HomeAdministrativeArea;
import org.example.repository.HomeAdministrativeAreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeAdministrativeAreaService {

    private final HomeAdministrativeAreaRepository repository;
    private final HomeAdministrativeAreaMapper mapper;
    private final AccessScopeService accessScopeService;

    @Transactional(readOnly = true)
    public Optional<HomeAdministrativeArea> getById(UUID id) {
        UUID userId = accessScopeService.requireCurrentUserId();
        if (!repository.isVisibleToUser(id, userId)) {
            return Optional.empty();
        }
        return repository.findById(id)
                .map(mapper::toApiModel);
    }

    @Transactional(readOnly = true)
    public List<HomeAdministrativeArea> listAll() {
        UUID userId = accessScopeService.requireCurrentUserId();
        return repository.findAllVisibleToUser(userId).stream()
                .map(mapper::toApiModel)
                .toList();
    }
}
