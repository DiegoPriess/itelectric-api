package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequestDTO;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository repository;
    private final WorkService workService;
    private final UserService userService;

    @Autowired
    public BudgetService(BudgetRepository repository, WorkService workService, UserService userService) {
        this.repository = repository;
        this.workService = workService;
        this.userService = userService;
    }

    public Budget create(BudgetRequestDTO budgetRequestDTO) {
        List<Work> workList = workService.getAllWorkSelectedById(budgetRequestDTO.getWorkIdList());
        User customer = userService.getUserById(budgetRequestDTO.getCustomerId());

        Budget budget = Budget.builder()
                              .workList(workList)
                              .customer(customer)
                              .deliveryForecast(budgetRequestDTO.getDeliveryForecast())
                              .build();

        return repository.save(budget);
    }

    public Budget update(Long budgetId, BudgetRequestDTO budgetRequestDTO) {
        Budget budget = getById(budgetId);

        List<Work> workList = workService.getAllWorkSelectedById(budgetRequestDTO.getWorkIdList());
        User customer = userService.getUserById(budgetRequestDTO.getCustomerId());

        budget.setWorkList(workList);
        budget.setCustomer(customer);
        budget.setDeliveryForecast(budgetRequestDTO.getDeliveryForecast());

        return repository.save(budget);
    }

    public void delete(Long userId) {
        Budget budget = repository.findById(userId)
                                  .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));

        repository.delete(budget);
    }

    public Budget getById(Long userId) {
        return repository.findById(userId)
                         .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));
    }

    public Page<Budget> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
