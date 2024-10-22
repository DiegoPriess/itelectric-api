package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enum")
public class EnumController {

    @GetMapping("/unitOfMeasure")
    public ResponseEntity<UnitOfMeasure[]> listUnitOfMeasure() {
        return new ResponseEntity<>(UnitOfMeasure.values(), HttpStatus.OK);
    }
}
