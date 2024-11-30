package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.dtos.enums.EnumDTO;
import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Enums", description = "Operações para recuperar enums utilizados na aplicação")
@RestController
@RequestMapping("/enum")
public class EnumController {

    @Operation(summary = "Listar unidades de medida", description = "Retorna todas as unidades de medida (UnitOfMeasure) disponíveis")
    @GetMapping("/unitOfMeasure")
    public ResponseEntity<List<EnumDTO>> listUnitOfMeasure() {
        List<EnumDTO> enumDTOS = Arrays.stream(UnitOfMeasure.values())
                .map(unit -> new EnumDTO(unit.name(), unit.getLabel()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(enumDTOS, HttpStatus.OK);
    }
}
