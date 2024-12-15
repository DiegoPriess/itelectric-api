package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.dtos.material.MaterialResponse;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.services.MaterialService;
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

@Tag(name = "Materiais", description = "Operações para gerenciamento de materiais")
@RestController
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(final MaterialService materialService) {
        this.materialService = materialService;
    }

    @Operation(summary = "Criar material", description = "Cria um novo material no sistema")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated Material material) {
        materialService.create(material);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar material", description = "Atualiza os dados de um material existente")
    @PutMapping("/{materialId}")
    public ResponseEntity<Void> update(@Parameter(description = "ID do material a ser atualizado") @PathVariable Long materialId,
                                       @RequestBody @Validated Material material) {
        materialService.update(materialId, material);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Excluir material", description = "Exclui um material do sistema pelo ID")
    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do material a ser excluído") @PathVariable Long materialId) {
        materialService.delete(materialId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Buscar material por ID", description = "Recupera um material específico pelo seu ID")
    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialResponse> getById(@Parameter(description = "ID do material") @PathVariable Long materialId) {
        MaterialResponse material = MaterialResponse.convert(materialService.getById(materialId));
        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @Operation(summary = "Listar materiais", description = "Lista os materiais com paginação e filtro opcional pelo nome")
    @GetMapping
    public ResponseEntity<Page<MaterialResponse>> list(@RequestParam(required = false) String name,
                                                       Pageable pageable) {
        Page<MaterialResponse> materialList = materialService.list(name, pageable);
        return new ResponseEntity<>(materialList, HttpStatus.OK);
    }
}
