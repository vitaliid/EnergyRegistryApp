package org.example.mappers;

import org.example.domain.AdministrationUnit;
import org.example.domain.AdministrationUnitAttribute;
import org.example.dto.AdministrationUnitAttributeResponse;
import org.example.dto.AdministrationUnitRequest;
import org.example.dto.AdministrationUnitResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AdministrationUnitAttributeMapper.class
)
public interface AdministrationUnitMapper {

    @Mapping(source = "parent.id", target = "parentId")
    AdministrationUnitResponse toResponse(AdministrationUnit entity);

    List<AdministrationUnitAttributeResponse> toResponseList(
            List<AdministrationUnitAttribute> entities);

    // REQUEST → ENTITY (for CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    AdministrationUnit toEntity(AdministrationUnitRequest request);
}
