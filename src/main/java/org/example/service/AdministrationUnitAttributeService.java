package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.AdministrationUnit;
import org.example.domain.AdministrationUnitAttribute;
import org.example.repository.AdministrationUnitAttributeRepository;
import org.example.repository.AdministrationUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministrationUnitAttributeService {

    private final AdministrationUnitAttributeRepository repository;
    private final AdministrationUnitRepository unitRepository;

    public List<AdministrationUnitAttribute> getByUnitId(Long unitId) {
        return repository.findByUnitId(unitId);
    }

    public AdministrationUnitAttribute create(Integer unitId, String key, String value) {

        AdministrationUnit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        AdministrationUnitAttribute attr = new AdministrationUnitAttribute();
        attr.setUnit(unit);
        attr.setKey(key);
        attr.setValue(value);

        return repository.save(attr);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}