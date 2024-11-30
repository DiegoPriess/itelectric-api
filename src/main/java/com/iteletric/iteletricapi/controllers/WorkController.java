package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.dtos.work.WorkResponse;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.services.WorkService;
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

@Tag(name = "Trabalhos", description = "Operações relacionadas à criação, atualização, exclusão e listagem de trabalhos")
@RestController
@RequestMapping("/work")
public class WorkController {

    private final WorkService service;

    @Autowired
    public WorkController(final WorkService service) {
        this.service = service;
    }

    @Operation(summary = "Criar trabalho", description = "Cria um novo trabalho no sistema")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated(ValidationGroups.POST.class) WorkRequest workRequest) {
        service.create(workRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar trabalho", description = "Atualiza um trabalho existente pelo ID")
    @PutMapping("/{workId}")
    public ResponseEntity<Void> update(@Parameter(description = "ID do trabalho a ser atualizado") @PathVariable Long workId,
                                       @RequestBody @Validated(ValidationGroups.PUT.class) WorkRequest workRequest) {
        service.update(workId, workRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Excluir trabalho", description = "Exclui um trabalho pelo ID")
    @DeleteMapping("/{workId}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do trabalho a ser excluído") @PathVariable Long workId) {
        service.delete(workId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Buscar trabalho por ID", description = "Recupera um trabalho específico pelo seu ID")
    @GetMapping("/{workId}")
    public ResponseEntity<Work> getById(@Parameter(description = "ID do trabalho") @PathVariable Long workId) {
        Work work = service.getById(workId);
        return new ResponseEntity<>(work, HttpStatus.OK);
    }

    @Operation(summary = "Listar trabalhos", description = "Lista trabalhos com paginação e filtro opcional pelo nome")
    @GetMapping
    public ResponseEntity<Page<WorkResponse>> list(@RequestParam(required = false) String name,
                                                   Pageable pageable) {
        Page<WorkResponse> workList = service.list(name, pageable);
        return new ResponseEntity<>(workList, HttpStatus.OK);
    }
}
