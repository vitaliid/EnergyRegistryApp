package org.example.mappers;

import org.example.domain.AdministrationUnit;
import org.example.dto.AdministrationUnitRequest;
import org.example.dto.AdministrationUnitResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministrationUnitMapper {

    @Mapping(source = "parent.id", target = "parentId")
    AdministrationUnitResponse toResponse(AdministrationUnit entity);

    // REQUEST → ENTITY (for CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    AdministrationUnit toEntity(AdministrationUnitRequest request);
}
