package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.AdministrationUnitType;

@Getter
@Setter
public class AdministrationUnitResponse {

    private int id;
    private AdministrationUnitType type;
    private String unitName;
    private Integer parentId;
}
