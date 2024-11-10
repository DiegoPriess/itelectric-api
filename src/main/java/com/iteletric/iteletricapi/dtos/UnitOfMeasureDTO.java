package com.iteletric.iteletricapi.dtos;

import lombok.Data;

@Data
public class UnitOfMeasureDTO {
    private String name;
    private String label;

    public UnitOfMeasureDTO(String name, String label) {
        this.name = name;
        this.label = label;
    }


}
