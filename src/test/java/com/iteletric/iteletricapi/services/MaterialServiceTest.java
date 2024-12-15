package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        user.setEmail("teste@gmail.com");

        material = Material.builder()
                .name("Cabo")
                .price(BigDecimal.valueOf(100))
                .unitMeasure(UnitOfMeasure.METERS)
                .quantityUnitMeasure(10)
                .build();
        material.setId(1L);
    }

    @Test
    void createMaterial_ShouldSaveMaterialSuccessfully() {
        materialService.create(material);

        verify(materialRepository).save(material);
    }

    @Test
    void updateMaterial_ShouldUpdateProperties() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        Material updatedDetails = Material.builder()
                .name("Cabo Atualizado")
                .price(BigDecimal.valueOf(150))
                .unitMeasure(UnitOfMeasure.CENTIMETERS)
                .quantityUnitMeasure(20)
                .build();

        materialService.update(1L, updatedDetails);

        assertEquals("Cabo Atualizado", material.getName());
        assertEquals(BigDecimal.valueOf(150), material.getPrice());
        assertEquals(UnitOfMeasure.CENTIMETERS, material.getUnitMeasure());
        assertEquals(20, material.getQuantityUnitMeasure());

        verify(materialRepository).save(material);
    }

    @Test
    void updateMaterial_ShouldThrowExceptionIfMaterialNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> {
            materialService.update(1L, material);
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
    void deleteMaterial_ShouldThrowExceptionIfMaterialNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> {
            materialService.delete(1L);
        });

        assertEquals("Material não encontrado", exception.getMessage());
    }

    @Test
    void getById_ShouldReturnMaterial() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        Material result = materialService.getById(1L);

        assertNotNull(result);
        assertEquals("Cabo", result.getName());
    }

    @Test
    void getById_ShouldThrowExceptionIfNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> materialService.getById(1L));

        assertEquals("Material não encontrado", exception.getMessage());
    }

    @Test
    void listMaterials_ShouldReturnAllMaterials() {
        when(materialRepository.findAllById(List.of(1L))).thenReturn(List.of(material));

        List<Material> result = materialService.getAllMaterialSelectedById(List.of(1L));

        assertNotNull(result, "A lista de materiais não deveria ser nula.");
        assertEquals(1, result.size(), "A lista deveria conter exatamente 1 material.");
        assertEquals("Cabo", result.get(0).getName(), "O nome do material não corresponde ao esperado.");
    }

    @Test
    void listMaterials_ShouldReturnEmptyIfNoMaterialFound() {
        List<Material> result = materialService.getAllMaterialSelectedById(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
