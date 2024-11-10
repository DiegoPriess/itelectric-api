package com.iteletric.iteletricapi.enums.material;

import com.iteletric.iteletricapi.dtos.UnitOfMeasureDTO;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum UnitOfMeasure {

    METERS("Metros"),
    CENTIMETERS("Centimetros"),
    UNIT("Unidade"),
    KG("Quilos");

    private final String label;

    UnitOfMeasure(String label) {
        this.label = label;
    }

    public static Optional<UnitOfMeasureDTO> getByName(String name) {
        return Arrays.stream(UnitOfMeasure.values())
                .filter(unit -> unit.name().equalsIgnoreCase(name))
                .map(unit -> new UnitOfMeasureDTO(unit.name(), unit.getLabel()))
                .findFirst();
    }
}
