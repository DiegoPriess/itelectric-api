package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.dtos.UnitOfMeasureDTO;
import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enum")
public class EnumController {

    @GetMapping("/unitOfMeasure")
    public ResponseEntity<List<UnitOfMeasureDTO>> listUnitOfMeasure() {
        List<UnitOfMeasureDTO> unitOfMeasureDTOs = Arrays.stream(UnitOfMeasure.values())
                                                         .map(unit -> new UnitOfMeasureDTO(unit.name(), unit.getLabel()))
                                                         .collect(Collectors.toList());

        return new ResponseEntity<>(unitOfMeasureDTOs, HttpStatus.OK);
    }
}
