package org.example.dto;

import org.example.domain.AdministrationUnitType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministrationUnitUpdateRequest {
    private AdministrationUnitType type;
    private String unitName;
    private Integer parentId;
}
