package org.example.repository;

import org.example.domain.AdministrationUnit;
import org.example.domain.AdministrationUnitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministrationUnitRepository extends JpaRepository<AdministrationUnit, Integer> {
    List<AdministrationUnit> findByType(AdministrationUnitType type);
}
