package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequest;
import com.iteletric.iteletricapi.dtos.budget.BudgetResponse;
import com.iteletric.iteletricapi.services.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gerenciamento de Orçamentos", description = "Operações para gerenciar orçamentos")
@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @Operation(summary = "Criar um novo orçamento", description = "Cria um novo orçamento com informações do cliente e detalhes do orçamento")
    @PostMapping
    public ResponseEntity<BudgetResponse> create(@RequestBody @Validated(ValidationGroups.POST.class) BudgetRequest budgetRequest) {
        budgetService.create(budgetRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar um orçamento existente", description = "Atualiza os detalhes de um orçamento para o ID especificado")
    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> update(@Parameter(description = "ID do orçamento a ser atualizado", required = true) @PathVariable Long budgetId,
                                                 @RequestBody @Validated(ValidationGroups.PUT.class) BudgetRequest budgetRequest) {
        budgetService.update(budgetId, budgetRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Excluir um orçamento existente", description = "Exclui um orçamento pelo ID especificado")
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do orçamento a ser excluído", required = true) @PathVariable Long budgetId) {
        budgetService.delete(budgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Obter um orçamento por ID", description = "Recupera os detalhes de um orçamento específico pelo seu ID")
    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> getById(@Parameter(description = "ID do orçamento a ser recuperado", required = true) @PathVariable Long budgetId) {
        BudgetResponse budget = BudgetResponse.convert(budgetService.getById(budgetId));
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos os orçamentos", description = "Obtém uma lista paginada de todos os orçamentos, com filtro opcional por e-mail do cliente")
    @GetMapping
    public ResponseEntity<Page<BudgetResponse>> list(@RequestParam(required = false) String customerEmail,
                                                     Pageable pageable) {
        Page<BudgetResponse> budgetList = BudgetResponse.convert(budgetService.list(customerEmail, pageable));
        return new ResponseEntity<>(budgetList, HttpStatus.OK);
    }

    @Operation(summary = "Listar orçamentos por cliente", description = "Obtém uma lista paginada de orçamentos para clientes")
    @GetMapping("/customer-list")
    public ResponseEntity<Page<BudgetResponse>> customerList(Pageable pageable) {
        Page<BudgetResponse> budgetList = BudgetResponse.convert(budgetService.customerList(pageable));
        return new ResponseEntity<>(budgetList, HttpStatus.OK);
    }

    @Operation(summary = "Aprovar um orçamento", description = "Aprova um orçamento pelo ID")
    @PutMapping("/{budgetId}/approve")
    public ResponseEntity<Void> approve(@Parameter(description = "ID do orçamento a ser aprovado", required = true) @PathVariable Long budgetId) {
        budgetService.approve(budgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Rejeitar um orçamento", description = "Rejeita um orçamento pelo ID")
    @PutMapping("/{budgetId}/deny")
    public ResponseEntity<Void> deny(@Parameter(description = "ID do orçamento a ser rejeitado", required = true) @PathVariable Long budgetId) {
        budgetService.deny(budgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
