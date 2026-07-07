package org.example.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnit;
import org.example.dto.AdministrationUnitSearchDto;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministrationUnitSearchService {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<AdministrationUnitSearchDto> search(String query, int limit) {
        var searchSession = Search.session(entityManager);

        return searchSession.search(AdministrationUnit.class)
                .where(f -> f.match()
                        .field("unitName")
                        .matching(query)
                        .fuzzy(1)
                )
                .fetchHits(limit)
                .stream()
                .map(unit -> new AdministrationUnitSearchDto(
                        unit.getId(),
                        unit.getUnitName(),
                        unit.getType().name()
                ))
                .toList();
    }
}