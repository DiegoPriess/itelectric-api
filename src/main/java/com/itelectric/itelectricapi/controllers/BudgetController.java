package com.itelectric.itelectricapi.controllers;

import com.itelectric.itelectricapi.config.validation.ValidationGroups;
import com.itelectric.itelectricapi.dtos.budget.BudgetRequest;
import com.itelectric.itelectricapi.dtos.budget.BudgetResponse;
import com.itelectric.itelectricapi.service.BudgetService;
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
    public ResponseEntity<BudgetResponse> create(@RequestBody @Validated(ValidationGroups.POST.class) BudgetRequest budgetRequest) {
        budgetService.create(budgetRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> update(@PathVariable Long budgetId, @RequestBody @Validated(ValidationGroups.PUT.class) BudgetRequest budgetRequest) {
        budgetService.update(budgetId, budgetRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> delete(@PathVariable Long budgetId) {
        budgetService.delete(budgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> getById(@PathVariable Long budgetId) {
        BudgetResponse budget = BudgetResponse.convert(budgetService.getById(budgetId));
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BudgetResponse>> list(@RequestParam(required = false) String customerEmail,
                                                     Pageable pageable) {
        Page<BudgetResponse> budgetList = BudgetResponse.convert(budgetService.list(customerEmail, pageable));
        return new ResponseEntity<>(budgetList, HttpStatus.OK);
    }

    @GetMapping("/customer-list")
    public ResponseEntity<Page<BudgetResponse>> customerList(Pageable pageable) {
        Page<BudgetResponse> budgetList = BudgetResponse.convert(budgetService.customerList(pageable));
        return new ResponseEntity<>(budgetList, HttpStatus.OK);
    }

    @PutMapping("/{budgetId}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long budgetId) {
        budgetService.approve(budgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{budgetId}/deny")
    public ResponseEntity<Void> deny(@PathVariable Long budgetId) {
        budgetService.deny(budgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
