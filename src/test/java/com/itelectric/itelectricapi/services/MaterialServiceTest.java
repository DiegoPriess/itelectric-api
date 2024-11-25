package com.itelectric.itelectricapi.services;

import com.itelectric.itelectricapi.config.exception.BusinessException;
import com.itelectric.itelectricapi.dtos.material.MaterialResponse;
import com.itelectric.itelectricapi.enums.material.UnitOfMeasure;
import com.itelectric.itelectricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.itelectric.itelectricapi.repositories.MaterialRepository;
import com.itelectric.itelectricapi.service.MaterialService;
import com.itelectric.itelectricapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MaterialService materialService;

    private Material material;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("diegopriess.dev@gmail.com");

        material = new Material();
        material.setId(1L);
        material.setName("Fio");
        material.setPrice(BigDecimal.valueOf(15.5));
        material.setUnitMeasure(UnitOfMeasure.CENTIMETERS);
        material.setQuantityUnitMeasure(BigDecimal.valueOf(100));
    }

    @Test
    void createMaterial_ShouldSaveMaterialSuccessfully() {
        materialService.create(material);
        verify(materialRepository).save(material);
    }

    @Test
    void updateMaterial_ShouldUpdateProperties() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        Material updatedDetails = new Material();
        updatedDetails.setName("Fio atualizado");
        updatedDetails.setPrice(BigDecimal.valueOf(15.0));
        updatedDetails.setUnitMeasure(UnitOfMeasure.METERS);
        updatedDetails.setQuantityUnitMeasure(BigDecimal.valueOf(200));

        materialService.update(1L, updatedDetails);

        assertEquals("Fio atualizado", material.getName());
        assertEquals(BigDecimal.valueOf(15.0), material.getPrice());
        assertEquals(UnitOfMeasure.METERS, material.getUnitMeasure());
        assertEquals(BigDecimal.valueOf(200), material.getQuantityUnitMeasure());
        verify(materialRepository).save(material);
    }

    @Test
    void updateMaterial_ShouldThrowExceptionIfNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> {
            materialService.update(1L, new Material());
        });

        assertEquals("Material não encontrado", exception.getMessage());
    }

    @Test
    void deleteMaterial_ShouldDeleteMaterial() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        materialService.delete(1L);

        verify(materialRepository).delete(material);
    }

    @Test
    void deleteMaterial_ShouldThrowExceptionIfNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> {
            materialService.delete(1L);
        });

        assertEquals("Material não encontrado", exception.getMessage());
    }

    @Test
    void getById_ShouldReturnMaterial() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        MaterialResponse result = materialService.getById(1L);

        assertNotNull(result);
        assertEquals("Fio", result.getName());
    }

    @Test
    void getById_ShouldThrowExceptionIfNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> {
            materialService.getById(1L);
        });

        assertEquals("Material não encontrado", exception.getMessage());
    }

    @Test
    void listMaterials_ShouldReturnPageOfMaterials() {
        when(userService.getCurrentUser()).thenReturn(user);
        Page<Material> page = new PageImpl<>(Collections.singletonList(material));
        when(materialRepository.findByOwner(user, PageRequest.of(0, 10, Sort.by("id").ascending()))).thenReturn(page);

        Page<MaterialResponse> result = materialService.list(null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(materialRepository).findByOwner(user, PageRequest.of(0, 10, Sort.by("id").ascending()));
    }

    @Test
    void listMaterials_WithValidName_ShouldReturnFilteredMaterials() {
        when(userService.getCurrentUser()).thenReturn(user);
        Page<Material> page = new PageImpl<>(Collections.singletonList(material));
        String searchName = "Fio";
        when(materialRepository.findByOwnerAndNameContainingIgnoreCase(
                eq(user), eq(searchName), any(Pageable.class)
        )).thenReturn(page);

        Page<MaterialResponse> result = materialService.list(searchName, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(materialRepository).findByOwnerAndNameContainingIgnoreCase(user, searchName, PageRequest.of(0, 10, Sort.by("id").ascending()));
    }

    @Test
    void listMaterials_WithEmptyName_ShouldReturnAllMaterials() {
        when(userService.getCurrentUser()).thenReturn(user);
        Page<Material> page = new PageImpl<>(Collections.singletonList(material));
        when(materialRepository.findByOwner(user, PageRequest.of(0, 10, Sort.by("id").ascending()))).thenReturn(page);

        Page<MaterialResponse> result = materialService.list("", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(materialRepository).findByOwner(user, PageRequest.of(0, 10, Sort.by("id").ascending()));
    }

    @Test
    void getAllMaterialSelectedById_WithValidIds_ShouldReturnMaterials() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(materialRepository.findAllById(ids)).thenReturn(Arrays.asList(material, new Material()));

        List<Material> result = materialService.getAllMaterialSelectedById(ids);

        assertEquals(2, result.size());
        verify(materialRepository).findAllById(ids);
    }

    @Test
    void getAllMaterialSelectedById_WithInvalidIds_ShouldThrowException() {
        List<Long> ids = List.of(3L);
        when(materialRepository.findAllById(ids)).thenReturn(List.of());

        Exception exception = assertThrows(BusinessException.class, () -> {
            materialService.getAllMaterialSelectedById(ids);
        });

        assertEquals("Os materiais selecionados não foram encontrados", exception.getMessage());
        verify(materialRepository).findAllById(ids);
    }
}
