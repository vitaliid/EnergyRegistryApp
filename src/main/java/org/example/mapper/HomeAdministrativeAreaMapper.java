package org.example.mapper;

import org.example.entity.HomeAdministrativeAreaEntity;
import org.example.entity.HomeAdministrativeAreaReferenceEntity;
import org.example.model.HomeAdministrativeArea;
import org.example.model.HomeAdministrativeAreaReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HomeAdministrativeAreaMapper {

    String OBJECT_TYPE = "HomeAdministrativeArea";

    @Mapping(target = "objectType", constant = OBJECT_TYPE)
    HomeAdministrativeArea toApiModel(HomeAdministrativeAreaEntity entity);

    /**
     * objectType is left unset: as a member of the AssignmentReference oneOf the type id is
     * written by the discriminator, not by the property.
     */
    @Mapping(target = "objectType", ignore = true)
    HomeAdministrativeAreaReference toReference(HomeAdministrativeAreaReferenceEntity entity);

    @Mapping(target = "objectType", ignore = true)
    HomeAdministrativeAreaReference toReference(HomeAdministrativeAreaEntity entity);
}
