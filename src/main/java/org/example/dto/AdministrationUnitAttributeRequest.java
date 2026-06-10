package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministrationUnitAttributeRequest {

    @NotBlank
    private String key;

    @NotBlank
    private String value;
}