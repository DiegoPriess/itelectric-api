package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.dtos.work.WorkResponse;
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
    public ResponseEntity<Void> create(@RequestBody @Validated(ValidationGroups.POST.class) WorkRequest workRequest) {
        service.create(workRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<Void> update(@PathVariable Long workId, @RequestBody @Validated(ValidationGroups.PUT.class) WorkRequest workRequest) {
        service.update(workId, workRequest);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<Page<WorkResponse>> list(@RequestParam(required = false) String name, Pageable pageable) {
        Page<WorkResponse> materialList = service.list(name, pageable);
        return new ResponseEntity<>(materialList, HttpStatus.OK);
    }
}
