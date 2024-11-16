package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private WorkService workService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWork() {
        WorkRequest workRequest = new WorkRequest("Trabalho 1", new BigDecimal("100.0"), Arrays.asList(1L, 2L));
        List<Material> materialList = Arrays.asList(new Material(), new Material());

        when(materialService.getAllMaterialSelectedById(anyList())).thenReturn(materialList);
        when(workRepository.save(any(Work.class))).thenReturn(new Work());

        Work createdWork = workService.create(workRequest);

        assertNotNull(createdWork);
        verify(materialService, times(1)).getAllMaterialSelectedById(workRequest.getMaterialIdList());
        verify(workRepository, times(1)).save(any(Work.class));
    }

    @Test
    void testUpdateWork() {
        Long workId = 1L;
        WorkRequest workRequest = new WorkRequest("Trabalho Atualizado", new BigDecimal("200.0"), Arrays.asList(1L, 2L));
        Work work = new Work();
        List<Material> materialList = Arrays.asList(new Material(), new Material());

        when(workRepository.findById(workId)).thenReturn(Optional.of(work));
        when(materialService.getAllMaterialSelectedById(anyList())).thenReturn(materialList);
        when(workRepository.save(any(Work.class))).thenReturn(work);

        Work updatedWork = workService.update(workId, workRequest);

        assertNotNull(updatedWork);
        assertEquals(workRequest.getName(), updatedWork.getName());
        verify(workRepository, times(1)).findById(workId);
        verify(workRepository, times(1)).save(any(Work.class));
    }

    @Test
    void testDeleteWork() {
        Long workId = 1L;
        Work work = new Work();

        when(workRepository.findById(workId)).thenReturn(Optional.of(work));
        doNothing().when(workRepository).delete(work);

        workService.delete(workId);

        verify(workRepository, times(1)).findById(workId);
        verify(workRepository, times(1)).delete(work);
    }

    @Test
    void testDeleteWork_NotFound() {
        Long workId = 1L;

        when(workRepository.findById(workId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> workService.delete(workId));
        assertEquals("Serviço não encontrado", exception.getMessage());
    }

    @Test
    void testGetWorkById() {
        Long workId = 1L;
        Work work = new Work();

        when(workRepository.findById(workId)).thenReturn(Optional.of(work));

        Work foundWork = workService.getById(workId);

        assertNotNull(foundWork);
        verify(workRepository, times(1)).findById(workId);
    }

    @Test
    void testGetWorkById_NotFound() {
        Long workId = 1L;

        when(workRepository.findById(workId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> workService.getById(workId));
        assertEquals("Serviço não encontrado", exception.getMessage());
    }

    @Test
    void testGetAllWorkSelectedById() {
        List<Long> workIdList = Arrays.asList(1L, 2L);
        Work work1 = new Work();
        Work work2 = new Work();
        List<Work> workList = Arrays.asList(work1, work2);

        when(workRepository.findAllById(workIdList)).thenReturn(workList);

        List<Work> result = workService.getAllWorkSelectedById(workIdList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(workRepository, times(1)).findAllById(workIdList);
    }

    @Test
    void testGetAllWorkSelectedById_NotFound() {
        List<Long> workIdList = Arrays.asList(1L, 2L);

        when(workRepository.findAllById(workIdList)).thenReturn(List.of());

        BusinessException exception = assertThrows(BusinessException.class, () -> workService.getAllWorkSelectedById(workIdList));
        assertEquals("Os serviços selecionados não foram encontrados", exception.getMessage());
    }
}
