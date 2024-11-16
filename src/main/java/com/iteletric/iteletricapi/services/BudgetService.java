package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequest;
import com.iteletric.iteletricapi.enums.budget.BudgetStatus;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
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

    public Budget create(BudgetRequest budgetRequest) {
        User customer = userService.createCustomerIfNecessary(budgetRequest.getCustomerEmail());
        List<Work> workList = workService.getAllWorkSelectedById(budgetRequest.getWorkIdList());

        Budget budget = Budget.builder()
                .workList(workList)
                .customer(customer)
                .deliveryForecast(budgetRequest.getDeliveryForecast())
                .build();

        return repository.save(budget);
    }

    public Budget update(Long budgetId, BudgetRequest budgetRequest) {
        Budget budget = getById(budgetId);

        User customer = userService.createCustomerIfNecessary(budgetRequest.getCustomerEmail());
        List<Work> workList = workService.getAllWorkSelectedById(budgetRequest.getWorkIdList());

        budget.setWorkList(workList);
        budget.setCustomer(customer);
        budget.setDeliveryForecast(budgetRequest.getDeliveryForecast());

        return repository.save(budget);
    }

    public void delete(Long userId) {
        Budget budget = repository.findById(userId)
                .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));

        repository.delete(budget);
    }

    public Budget getById(Long budgetId) {
        return repository.findById(budgetId)
                .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));
    }

    public Page<Budget> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void approve(Long budgetId) {
        Budget budget = repository.findById(budgetId)
                .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));

        budget.setStatus(BudgetStatus.APPROVED);
        repository.save(budget);
    }

    public void deny(Long budgetId) {
        Budget budget = repository.findById(budgetId)
                .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));

        budget.setStatus(BudgetStatus.DENIED);
        repository.save(budget);
    }
}
