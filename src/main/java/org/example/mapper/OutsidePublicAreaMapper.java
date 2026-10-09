package org.example.mapper;

import org.example.entity.OutsidePublicAreaEntity;
import org.example.model.OutsidePublicArea;
import org.example.model.OutsidePublicAreaReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = HomeAdministrativeAreaMapper.class)
public interface OutsidePublicAreaMapper {

    OutsidePublicArea toApiModel(OutsidePublicAreaEntity entity);

    @Mapping(target = "objectType", ignore = true)
    OutsidePublicAreaReference toReference(OutsidePublicAreaEntity entity);
}
