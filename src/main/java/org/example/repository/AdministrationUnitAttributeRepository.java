package org.example.repository;

import org.example.domain.AdministrationUnitAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministrationUnitAttributeRepository
        extends JpaRepository<AdministrationUnitAttribute, Long> {

    List<AdministrationUnitAttribute> findByUnitId(Long unitId);
}