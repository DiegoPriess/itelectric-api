package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.dtos.budget.BudgetRequestDTO;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.services.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Budget> create(@RequestBody @Valid BudgetRequestDTO budgetRequestDTO) {
        Budget createdBudget = budgetService.create(budgetRequestDTO);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> update(@PathVariable Long id, @RequestBody @Valid BudgetRequestDTO budgetRequestDTO) {
        Budget updatedBudget = budgetService.update(id, budgetRequestDTO);
        return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getById(@PathVariable Long id) {
        Budget budget = budgetService.getById(id);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Budget>> list(Pageable pageable) {
        Page<Budget> budgets = budgetService.list(pageable);
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }
}
