package com.iteletric.iteletricapi.enums.budget;

import com.iteletric.iteletricapi.dtos.enums.EnumDTO;
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
        return Arrays.stream(com.iteletric.iteletricapi.enums.material.UnitOfMeasure.values())
                .filter(unit -> unit.name().equalsIgnoreCase(name))
                .map(unit -> new EnumDTO(unit.name(), unit.getLabel()))
                .findFirst();
    }
}
