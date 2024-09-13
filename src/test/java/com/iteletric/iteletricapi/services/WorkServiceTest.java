package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class WorkServiceTest {

    @Mock
    private WorkRepository repository;

    @InjectMocks
    private WorkService workService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWork() {
        Work work = new Work();
        work.setName("Test Work");
        work.setPrice(BigDecimal.valueOf(1000.0));

        workService.create(work);

        verify(repository).save(work);
    }

    @Test
    void testUpdateWorkNotFound() {
        Work workDetails = new Work();
        workDetails.setName("Updated Work");
        workDetails.setPrice(BigDecimal.valueOf(1500.0));

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            workService.update(1L, workDetails);
        });

        assertEquals("Trabalho não encontrado", thrown.getMessage());
    }

    @Test
    void testUpdateWorkSuccess() {
        Work work = new Work();
        work.setName("Old Work");
        work.setPrice(BigDecimal.valueOf(1000.0));

        Work workDetails = new Work();
        workDetails.setName("Updated Work");
        workDetails.setPrice(BigDecimal.valueOf(1500.0));

        when(repository.findById(anyLong())).thenReturn(Optional.of(work));

        workService.update(1L, workDetails);

        assertEquals("Updated Work", work.getName());
        assertEquals(BigDecimal.valueOf(1500.0), work.getPrice());
        verify(repository).save(work);
    }

    @Test
    void testDeleteWorkNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            workService.delete(1L);
        });

        assertEquals("Trabalho não encontrado", thrown.getMessage());
    }

    @Test
    void testDeleteWorkSuccess() {
        Work work = new Work();
        when(repository.findById(anyLong())).thenReturn(Optional.of(work));

        workService.delete(1L);

        verify(repository).delete(work);
    }

    @Test
    void testGetByIdSuccess() {
        Work work = new Work();
        when(repository.findById(anyLong())).thenReturn(Optional.of(work));

        Work result = workService.getById(1L);

        assertEquals(work, result);
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            workService.getById(1L);
        });

        assertEquals("Trabalho não encontrado", thrown.getMessage());
    }

    @Test
    void testListWorks() {
        Pageable pageable = mock(Pageable.class);
        List<Work> works = List.of(new Work(), new Work());
        Page<Work> workPage = new PageImpl<>(works);
        when(repository.findAll(pageable)).thenReturn(workPage);

        Page<Work> result = workService.list(pageable);

        assertEquals(workPage, result);
    }
}
