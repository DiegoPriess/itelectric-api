package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequestDTO;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> create(@RequestBody @Validated(ValidationGroups.POST.class) BudgetRequestDTO budgetRequestDTO) {
        Budget createdBudget = budgetService.create(budgetRequestDTO);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<Budget> update(@PathVariable Long budgetId, @RequestBody @Validated(ValidationGroups.PUT.class) BudgetRequestDTO budgetRequestDTO) {
        Budget updatedBudget = budgetService.update(budgetId, budgetRequestDTO);
        return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> delete(@PathVariable Long budgetId) {
        budgetService.delete(budgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<Budget> getById(@PathVariable Long budgetId) {
        Budget budget = budgetService.getById(budgetId);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Budget>> list(Pageable pageable) {
        Page<Budget> budgetList = budgetService.list(pageable);
        return new ResponseEntity<>(budgetList, HttpStatus.OK);
    }
}
