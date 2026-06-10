package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.AdministrationUnitType;
import org.example.mappers.AdministrationUnitAttributeMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdministrationUnitResponse {

    private int id;
    private AdministrationUnitType type;
    private String unitName;
    private Integer parentId;

    private List<AdministrationUnitAttributeResponse> attributes = new ArrayList<>();
}
