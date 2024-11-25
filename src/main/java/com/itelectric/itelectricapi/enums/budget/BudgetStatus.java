package com.itelectric.itelectricapi.enums.budget;

import com.itelectric.itelectricapi.dtos.enums.EnumDTO;
import com.itelectric.itelectricapi.enums.material.UnitOfMeasure;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum BudgetStatus {

    PENDING("Pendente"),
    APPROVED("Aprovado"),
    DENIED("Negado");

    private final String label;

    BudgetStatus(String label) {
        this.label = label;
    }

    public static Optional<EnumDTO> getByName(String name) {
        return Arrays.stream(UnitOfMeasure.values())
                .filter(unit -> unit.name().equalsIgnoreCase(name))
                .map(unit -> new EnumDTO(unit.name(), unit.getLabel()))
                .findFirst();
    }
}
