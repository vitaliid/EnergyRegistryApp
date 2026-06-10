package org.example.mappers;

import org.example.domain.AdministrationUnitAttribute;
import org.example.dto.AdministrationUnitAttributeRequest;
import org.example.dto.AdministrationUnitAttributeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministrationUnitAttributeMapper {

    @Mapping(source = "unit.id", target = "unitId")
    AdministrationUnitAttributeResponse toResponse(AdministrationUnitAttribute entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unit", ignore = true)
    AdministrationUnitAttribute toEntity(AdministrationUnitAttributeRequest request);
}