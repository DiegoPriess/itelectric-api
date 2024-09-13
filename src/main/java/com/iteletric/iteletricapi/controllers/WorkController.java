package com.iteletric.iteletricapi.controllers;

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

    private final WorkService workService;

    @Autowired
    WorkController(final WorkService workService) {
        this.workService = workService;
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody @Valid Work work) {
        workService.create(work);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid Work work) {
        workService.update(id, work);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Work> getById(@PathVariable Long id) {
        Work work = workService.getById(id);
        return new ResponseEntity<>(work, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Work>> list(Pageable pageable) {
        Page<Work> works = workService.list(pageable);
        return new ResponseEntity<>(works, HttpStatus.OK);
    }
}
