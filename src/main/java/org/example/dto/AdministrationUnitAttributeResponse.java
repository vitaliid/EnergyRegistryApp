package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministrationUnitAttributeResponse {

    private Long id;
    private String key;
    private String value;
    private Integer unitId;
}