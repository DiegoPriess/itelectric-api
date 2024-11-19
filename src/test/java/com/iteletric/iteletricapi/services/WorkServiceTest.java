package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @Mock
    private MaterialService materialService;

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkService workService;

    private Work work;
    private Material material;

    @BeforeEach
    void setUp() {
        material = new Material();
        material.setId(1L);
        material.setName("Fio");

        work = new Work();
        work.setId(1L);
        work.setName("Instalação");
        work.setPrice(BigDecimal.valueOf(100.00));
        work.setMaterialList(Collections.singletonList(material));
    }

    @Test
    void create_ShouldCreateWorkSuccessfully() {
        WorkRequest request = new WorkRequest();
        request.setName("Fio 2");
        request.setPrice(BigDecimal.valueOf(150.00));
        request.setMaterialIdList(List.of(1L));

        when(materialService.getAllMaterialSelectedById(request.getMaterialIdList())).thenReturn(Collections.singletonList(material));

        workService.create(request);

        verify(workRepository).save(any(Work.class));
    }

    @Test
    void create_ShouldThrowExceptionWhenNoMaterials() {
        WorkRequest request = new WorkRequest();
        request.setName("Fio");
        request.setPrice(BigDecimal.valueOf(150.00));
        request.setMaterialIdList(List.of());

        Exception exception = assertThrows(BusinessException.class, () -> workService.create(request));
        assertEquals("Para criar um trabalho, é necessário selecionar pelo menos um material", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateWorkSuccessfully() {
        WorkRequest request = new WorkRequest();
        request.setName("Cabo elétrico");
        request.setPrice(BigDecimal.valueOf(200.00));
        request.setMaterialIdList(List.of(1L));

        when(workRepository.findById(1L)).thenReturn(Optional.of(work));
        when(materialService.getAllMaterialSelectedById(request.getMaterialIdList())).thenReturn(Collections.singletonList(material));

        workService.update(1L, request);

        assertEquals("Cabo elétrico", work.getName());
        assertEquals(BigDecimal.valueOf(200.00), work.getPrice());
        verify(workRepository).save(work);
    }

    @Test
    void update_ShouldThrowExceptionWhenNoMaterials() {
        WorkRequest request = new WorkRequest();
        request.setMaterialIdList(List.of());

        lenient().when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        Exception exception = assertThrows(BusinessException.class, () -> workService.update(1L, request));
        assertEquals("Para alterar um trabalho, é necessário manter pelo menos um material", exception.getMessage());
    }


    @Test
    void delete_ShouldDeleteWork() {
        when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        workService.delete(1L);

        verify(workRepository).delete(work);
    }

    @Test
    void delete_ShouldThrowExceptionWhenWorkNotFound() {
        when(workRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> workService.delete(1L));
        assertEquals("Serviço não encontrado", exception.getMessage());
    }

    @Test
    void getById_ShouldReturnWork() {
        when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        Work result = workService.getById(1L);

        assertNotNull(result);
        assertEquals("Instalação", result.getName());
    }

    @Test
    void getById_ShouldThrowExceptionIfNotFound() {
        when(workRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> workService.getById(1L));
        assertEquals("Serviço não encontrado", exception.getMessage());
    }

    @Test
    void getAllWorkSelectedById_ShouldReturnWorks() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(workRepository.findAllById(ids)).thenReturn(Arrays.asList(work, new Work()));

        List<Work> result = workService.getAllWorkSelectedById(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllWorkSelectedById_ShouldThrowExceptionWhenEmpty() {
        List<Long> ids = List.of(3L);
        when(workRepository.findAllById(ids)).thenReturn(List.of());

        Exception exception = assertThrows(BusinessException.class, () -> workService.getAllWorkSelectedById(ids));
        assertEquals("Os serviços selecionados não foram encontrados", exception.getMessage());
    }

}
