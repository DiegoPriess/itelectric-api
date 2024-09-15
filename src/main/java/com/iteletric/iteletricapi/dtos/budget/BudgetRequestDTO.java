package com.iteletric.iteletricapi.dtos.budget;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BudgetRequestDTO {
    private List<Long> workIdList;
    private LocalDate deliveryForecast;
    private Long customerId;
}
