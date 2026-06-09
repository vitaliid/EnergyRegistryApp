package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.AdministrationUnitType;

@Getter
@Setter
public class AdministrationUnitRequest {

    private AdministrationUnitType type;
    private String unitName;
    private Integer parentId;
}
