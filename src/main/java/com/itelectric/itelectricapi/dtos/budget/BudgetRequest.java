package com.itelectric.itelectricapi.dtos.budget;

import com.itelectric.itelectricapi.config.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BudgetRequest {
    private List<Long> workIdList;

    private LocalDate deliveryForecast;

    @NotNull(groups = ValidationGroups.POST.class, message = "O cliente deve ser informado!")
    private String customerEmail;
}
