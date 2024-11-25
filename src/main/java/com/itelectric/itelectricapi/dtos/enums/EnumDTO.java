package com.itelectric.itelectricapi.dtos.enums;

import lombok.Data;

@Data
public class EnumDTO {
    private String name;
    private String label;

    public EnumDTO(String name, String label) {
        this.name = name;
        this.label = label;
    }
}
