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

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid Material material) {
        materialService.update(id, material);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(@PathVariable Long id) {
        Material material = materialService.getById(id);
        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Material>> list(Pageable pageable) {
        Page<Material> materials = materialService.list(pageable);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }
}
