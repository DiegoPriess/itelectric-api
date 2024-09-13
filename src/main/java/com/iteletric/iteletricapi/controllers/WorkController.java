package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.dtos.work.WorkRequestDTO;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.services.WorkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work")
public class WorkController {

    private final WorkService service;

    @Autowired
    WorkController(final WorkService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Work> create(@RequestBody WorkRequestDTO workRequestDTO) {
        Work createdWork = service.create(workRequestDTO);
        return new ResponseEntity<>(createdWork, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Work> update(@PathVariable Long id, @RequestBody @Valid WorkRequestDTO workRequestDTO) {
        Work updatedWork = service.update(id, workRequestDTO);
        return new ResponseEntity<>(updatedWork, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Work> getById(@PathVariable Long id) {
        Work work = service.getById(id);
        return new ResponseEntity<>(work, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Work>> list(Pageable pageable) {
        Page<Work> works = service.list(pageable);
        return new ResponseEntity<>(works, HttpStatus.OK);
    }
}
