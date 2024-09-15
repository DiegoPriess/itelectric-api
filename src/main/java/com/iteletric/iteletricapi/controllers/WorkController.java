package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.dtos.work.WorkRequestDTO;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.services.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Work> create(@RequestBody @Validated(ValidationGroups.POST.class) WorkRequestDTO workRequestDTO) {
        Work createdWork = service.create(workRequestDTO);
        return new ResponseEntity<>(createdWork, HttpStatus.CREATED);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<Work> update(@PathVariable Long workId, @RequestBody @Validated(ValidationGroups.PUT.class) WorkRequestDTO workRequestDTO) {
        Work updatedWork = service.update(workId, workRequestDTO);
        return new ResponseEntity<>(updatedWork, HttpStatus.OK);
    }

    @DeleteMapping("/{workId}")
    public ResponseEntity<Void> delete(@PathVariable Long workId) {
        service.delete(workId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{workId}")
    public ResponseEntity<Work> getById(@PathVariable Long workId) {
        Work work = service.getById(workId);
        return new ResponseEntity<>(work, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Work>> list(Pageable pageable) {
        Page<Work> workList = service.list(pageable);
        return new ResponseEntity<>(workList, HttpStatus.OK);
    }
}
