package com.iteletric.iteletricapi.dtos.budget;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
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
