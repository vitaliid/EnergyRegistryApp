package org.example.repository;

import org.example.entity.EsrConsumptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EsrConsumptionRepository extends JpaRepository<EsrConsumptionEntity, UUID> {

    List<EsrConsumptionEntity> findAllByOutsidePublicAreaIdOrderByStartAscCreatedAtAsc(UUID outsidePublicAreaId);

    Optional<EsrConsumptionEntity> findByIdAndOutsidePublicAreaId(UUID id, UUID outsidePublicAreaId);
}
