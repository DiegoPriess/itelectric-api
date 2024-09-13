package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    private Material material;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        material = Material.builder()
                .id(1L)
                .name("Cement")
                .price(BigDecimal.valueOf(20.00))
                .unitMeasure(UnitOfMeasure.METERS)
                .quantityUnitMeasure(BigDecimal.TEN)
                .build();
    }

    @Test
    void testCreateMaterial() {
        when(materialRepository.save(any(Material.class))).thenReturn(material);

        materialService.create(material);

        verify(materialRepository, times(1)).save(material);
    }

    @Test
    void testUpdateMaterial() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        Material updatedMaterialDetails = Material.builder()
                .name("Updated Cement")
                .price(BigDecimal.valueOf(25.00))
                .unitMeasure(UnitOfMeasure.METERS)
                .quantityUnitMeasure(BigDecimal.valueOf(100000))
                .build();

        materialService.update(1L, updatedMaterialDetails);

        assertEquals("Updated Cement", material.getName());
        assertEquals(BigDecimal.valueOf(25.00), material.getPrice());
        assertEquals(UnitOfMeasure.METERS, material.getUnitMeasure());
        assertEquals(BigDecimal.valueOf(100000), material.getQuantityUnitMeasure());

        verify(materialRepository, times(1)).save(material);
    }

    @Test
    void testUpdateMaterialNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Material updatedMaterialDetails = Material.builder()
                .name("Updated Cement")
                .price(BigDecimal.valueOf(25.00))
                .unitMeasure(UnitOfMeasure.CENTIMETERS)
                .quantityUnitMeasure(BigDecimal.TEN)
                .build();

        assertThrows(BusinessException.class, () -> materialService.update(1L, updatedMaterialDetails));
    }

    @Test
    void testDeleteMaterial() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        materialService.delete(1L);

        verify(materialRepository, times(1)).delete(material);
    }

    @Test
    void testDeleteMaterialNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> materialService.delete(1L));
    }

    @Test
    void testGetMaterialById() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        Material foundMaterial = materialService.getById(1L);

        assertEquals(material, foundMaterial);
        verify(materialRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMaterialByIdNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> materialService.getById(1L));
    }

    @Test
    void testListMaterials() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Material> materialPage = new PageImpl<>(Collections.singletonList(material));

        when(materialRepository.findAll(pageable)).thenReturn(materialPage);

        Page<Material> result = materialService.list(pageable);

        assertEquals(1, result.getTotalElements());
        verify(materialRepository, times(1)).findAll(pageable);
    }
}
