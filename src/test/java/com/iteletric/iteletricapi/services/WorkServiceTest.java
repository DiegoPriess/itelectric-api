package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequestDTO;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import com.iteletric.iteletricapi.repositories.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    private MaterialRepository materialRepository;

    @InjectMocks
    private WorkService workService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWorkSuccess() {
        WorkRequestDTO workRequestDTO = new WorkRequestDTO();
        workRequestDTO.setName("New Work");
        workRequestDTO.setPrice(BigDecimal.valueOf(1000));
        workRequestDTO.setMaterialIdList(Arrays.asList(1L, 2L));

        List<Material> materials = Arrays.asList(
                Material.builder().id(1L).name("Material 1").price(BigDecimal.valueOf(100)).build(),
                Material.builder().id(2L).name("Material 2").price(BigDecimal.valueOf(200)).build()
        );

        when(materialRepository.findAllById(workRequestDTO.getMaterialIdList())).thenReturn(materials);
        when(workRepository.save(any(Work.class))).thenReturn(Work.builder().id(1L).build());

        Work result = workService.create(workRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(workRepository).save(any(Work.class));
    }

    @Test
    void testCreateWorkNoMaterialsFound() {
        WorkRequestDTO workRequestDTO = new WorkRequestDTO();
        workRequestDTO.setMaterialIdList(Arrays.asList(1L, 2L));

        when(materialRepository.findAllById(workRequestDTO.getMaterialIdList())).thenReturn(Arrays.asList());

        BusinessException exception = assertThrows(BusinessException.class, () -> workService.create(workRequestDTO));
        assertEquals("Nenhum material encontrado com os IDs fornecidos", exception.getMessage());
    }

    @Test
    void testUpdateWorkSuccess() {
        Long workId = 1L;
        WorkRequestDTO workRequestDTO = new WorkRequestDTO();
        workRequestDTO.setName("Updated Work");
        workRequestDTO.setPrice(BigDecimal.valueOf(1500));
        workRequestDTO.setMaterialIdList(Arrays.asList(1L, 2L));

        List<Material> materials = Arrays.asList(
                Material.builder().id(1L).name("Material 1").price(BigDecimal.valueOf(100)).build(),
                Material.builder().id(2L).name("Material 2").price(BigDecimal.valueOf(200)).build()
        );

        Work existingWork = Work.builder().id(workId).build();

        when(workRepository.findById(workId)).thenReturn(Optional.of(existingWork));
        when(materialRepository.findAllById(workRequestDTO.getMaterialIdList())).thenReturn(materials);
        when(workRepository.save(any(Work.class))).thenReturn(existingWork);

        Work updatedWork = workService.update(workId, workRequestDTO);

        assertNotNull(updatedWork);
        assertEquals(workId, updatedWork.getId());
        verify(workRepository).save(existingWork);
    }

    @Test
    void testUpdateWorkNotFound() {
        Long workId = 1L;
        WorkRequestDTO workRequestDTO = new WorkRequestDTO();
        workRequestDTO.setMaterialIdList(Arrays.asList(1L, 2L));

        when(workRepository.findById(workId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> workService.update(workId, workRequestDTO));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteWorkSuccess() {
        Long workId = 1L;
        Work work = Work.builder().id(workId).build();

        when(workRepository.findById(workId)).thenReturn(Optional.of(work));

        workService.delete(workId);

        verify(workRepository).delete(work);
    }

    @Test
    void testDeleteWorkNotFound() {
        Long workId = 1L;

        when(workRepository.findById(workId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> workService.delete(workId));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }

    @Test
    void testGetByIdSuccess() {
        Long workId = 1L;
        Work work = Work.builder().id(workId).build();

        when(workRepository.findById(workId)).thenReturn(Optional.of(work));

        Work result = workService.getById(workId);

        assertNotNull(result);
        assertEquals(workId, result.getId());
    }

    @Test
    void testGetByIdNotFound() {
        Long workId = 1L;

        when(workRepository.findById(workId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> workService.getById(workId));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }

    @Test
    void testListWorks() {
        Pageable pageable = mock(Pageable.class);
        List<Work> works = Arrays.asList(Work.builder().id(1L).build(), Work.builder().id(2L).build());
        Page<Work> workPage = new PageImpl<>(works);

        when(workRepository.findAll(pageable)).thenReturn(workPage);

        Page<Work> result = workService.list(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }
}
