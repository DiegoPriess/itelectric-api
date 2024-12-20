package com.iteletric.iteletricapi.dtos.budget;

import com.iteletric.iteletricapi.dtos.enums.EnumDTO;
import com.iteletric.iteletricapi.dtos.user.UserResponse;
import com.iteletric.iteletricapi.dtos.work.WorkResponse;
import com.iteletric.iteletricapi.models.Budget;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BudgetResponse {
    private Long id;
    private List<WorkResponse> workList;
    private LocalDate deliveryForecast;
    private UserResponse customer;
    private BigDecimal totalValue;
    private EnumDTO status;
    private UserResponse owner;

    public static BudgetResponse convert(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .workList(WorkResponse.convert(budget.getWorkList()))
                .deliveryForecast(budget.getDeliveryForecast())
                .customer(UserResponse.convert(budget.getCustomer()))
                .totalValue(budget.getTotalValue())
                .status(new EnumDTO(budget.getStatus().toString(), budget.getStatus().getLabel()))
                .owner(UserResponse.convert(budget.getOwner()))
                .build();
    }

    public static Page<BudgetResponse> convert(Page<Budget> budgetPage) {
        return budgetPage.map(budget ->
                BudgetResponse.builder()
                        .id(budget.getId())
                        .workList(WorkResponse.convert(budget.getWorkList()))
                        .deliveryForecast(budget.getDeliveryForecast())
                        .customer(UserResponse.convert(budget.getCustomer()))
                        .totalValue(budget.getTotalValue())
                        .status(new EnumDTO(budget.getStatus().toString(), budget.getStatus().getLabel()))
                        .owner(UserResponse.convert(budget.getOwner()))
                        .build()
        );
    }
}
