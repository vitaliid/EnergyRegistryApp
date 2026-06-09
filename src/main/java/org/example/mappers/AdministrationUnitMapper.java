package org.example.mappers;

import org.example.domain.AdministrationUnit;
import org.example.dto.AdministrationUnitResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministrationUnitMapper {

    @Mapping(source = "parent.id", target = "parentId")
    AdministrationUnitResponse toResponse(AdministrationUnit entity);
}
