package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.services.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    MaterialController(final MaterialService materialService) {
        this.materialService = materialService;
    }

    private final MaterialService materialService;

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody @Valid Material material) {
        materialService.create(material);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<Void> update(@PathVariable Long materialId, @RequestBody @Valid Material material) {
        materialService.update(materialId, material);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> delete(@PathVariable Long materialId) {
        materialService.delete(materialId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<Material> getById(@PathVariable Long materialId) {
        Material material = materialService.getById(materialId);
        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Material>> list(Pageable pageable) {
        Page<Material> materialList = materialService.list(pageable);
        return new ResponseEntity<>(materialList, HttpStatus.OK);
    }
}
