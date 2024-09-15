package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.repositories.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialServiceTest {

    @Mock
    private MaterialRepository repository;

    @InjectMocks
    private MaterialService materialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMaterialSuccess() {
        Material material = new Material();
        materialService.create(material);
        verify(repository, times(1)).save(material);
    }

    @Test
    void updateMaterialSuccess() {
        Long materialId = 1L;
        Material materialDetails = new Material();
        Material existingMaterial = new Material();

        when(repository.findById(materialId)).thenReturn(Optional.of(existingMaterial));
        materialService.update(materialId, materialDetails);
        verify(repository, times(1)).save(existingMaterial);
    }

    @Test
    void updateMaterialNotFound() {
        Long materialId = 1L;
        when(repository.findById(materialId)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> materialService.update(materialId, new Material()));
    }

    @Test
    void deleteMaterialSuccess() {
        Long materialId = 1L;
        Material existingMaterial = new Material();

        when(repository.findById(materialId)).thenReturn(Optional.of(existingMaterial));
        materialService.delete(materialId);
        verify(repository, times(1)).delete(existingMaterial);
    }

    @Test
    void deleteMaterialNotFound() {
        Long materialId = 1L;
        when(repository.findById(materialId)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> materialService.delete(materialId));
    }

    @Test
    void getByIdMaterialSuccess() {
        Long materialId = 1L;
        Material material = new Material();
        when(repository.findById(materialId)).thenReturn(Optional.of(material));
        Material result = materialService.getById(materialId);
        assertNotNull(result);
    }

    @Test
    void getByIdMaterialNotFound() {
        Long materialId = 1L;
        when(repository.findById(materialId)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> materialService.getById(materialId));
    }

    @Test
    void listMaterialsSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Material> page = new PageImpl<>(List.of(new Material()));
        when(repository.findAll(pageable)).thenReturn(page);
        Page<Material> result = materialService.list(pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllMaterialSelectedByIdSuccess() {
        List<Long> materialIdList = Arrays.asList(1L, 2L);
        List<Material> materialList = Arrays.asList(new Material(), new Material());
        when(repository.findAllById(materialIdList)).thenReturn(materialList);
        List<Material> result = materialService.getAllMaterialSelectedById(materialIdList);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllMaterialSelectedByIdNotFound() {
        List<Long> materialIdList = Arrays.asList(1L, 2L);
        when(repository.findAllById(materialIdList)).thenReturn(List.of());
        assertThrows(BusinessException.class, () -> materialService.getAllMaterialSelectedById(materialIdList));
    }
}
