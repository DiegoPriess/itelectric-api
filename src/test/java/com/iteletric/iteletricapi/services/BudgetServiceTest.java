package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequestDTO;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private WorkRepository workRepository;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBudgetSuccess() {
        BudgetRequestDTO budgetRequestDTO = new BudgetRequestDTO();
        budgetRequestDTO.setWorkIdList(Arrays.asList(1L, 2L));
        budgetRequestDTO.setDeliveryForecast(LocalDate.of(2024, 12, 31));

        List<Work> workList = Arrays.asList(
                Work.builder().id(1L).price(BigDecimal.valueOf(500)).build(),
                Work.builder().id(2L).price(BigDecimal.valueOf(600)).build()
        );

        when(workRepository.findAllById(budgetRequestDTO.getWorkIdList())).thenReturn(workList);
        when(budgetRepository.save(any(Budget.class))).thenReturn(Budget.builder().id(1L).build());

        Budget result = budgetService.create(budgetRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void testCreateBudgetNoWorksFound() {
        BudgetRequestDTO budgetRequestDTO = new BudgetRequestDTO();
        budgetRequestDTO.setWorkIdList(Arrays.asList(1L, 2L));

        when(workRepository.findAllById(budgetRequestDTO.getWorkIdList())).thenReturn(Arrays.asList());

        BusinessException exception = assertThrows(BusinessException.class, () -> budgetService.create(budgetRequestDTO));
        assertEquals("Nenhum trabalho encontrado com os IDs fornecidos", exception.getMessage());
    }

    @Test
    void testUpdateBudgetSuccess() {
        Long budgetId = 1L;
        BudgetRequestDTO budgetRequestDTO = new BudgetRequestDTO();
        budgetRequestDTO.setWorkIdList(Arrays.asList(1L, 2L));
        budgetRequestDTO.setDeliveryForecast(LocalDate.of(2024, 12, 31));

        List<Work> workList = Arrays.asList(
                Work.builder().id(1L).price(BigDecimal.valueOf(500)).build(),
                Work.builder().id(2L).price(BigDecimal.valueOf(600)).build()
        );

        Budget existingBudget = Budget.builder().id(budgetId).build();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(existingBudget));
        when(workRepository.findAllById(budgetRequestDTO.getWorkIdList())).thenReturn(workList);
        when(budgetRepository.save(any(Budget.class))).thenReturn(existingBudget);

        Budget updatedBudget = budgetService.update(budgetId, budgetRequestDTO);

        assertNotNull(updatedBudget);
        assertEquals(budgetId, updatedBudget.getId());
        verify(budgetRepository).save(existingBudget);
    }

    @Test
    void testUpdateBudgetNotFound() {
        Long budgetId = 1L;
        BudgetRequestDTO budgetRequestDTO = new BudgetRequestDTO();
        budgetRequestDTO.setWorkIdList(Arrays.asList(1L, 2L));

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> budgetService.update(budgetId, budgetRequestDTO));
        assertEquals("Orçamento não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteBudgetSuccess() {
        Long budgetId = 1L;
        Budget budget = Budget.builder().id(budgetId).build();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        budgetService.delete(budgetId);

        verify(budgetRepository).delete(budget);
    }

    @Test
    void testDeleteBudgetNotFound() {
        Long budgetId = 1L;

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> budgetService.delete(budgetId));
        assertEquals("Orçamento não encontrado", exception.getMessage());
    }

    @Test
    void testGetByIdSuccess() {
        Long budgetId = 1L;
        Budget budget = Budget.builder().id(budgetId).build();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        Budget result = budgetService.getById(budgetId);

        assertNotNull(result);
        assertEquals(budgetId, result.getId());
    }

    @Test
    void testGetByIdNotFound() {
        Long budgetId = 1L;

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> budgetService.getById(budgetId));
        assertEquals("Orçamento não encontrado", exception.getMessage());
    }

    @Test
    void testListBudgets() {
        Pageable pageable = mock(Pageable.class);
        List<Budget> budgetList = Arrays.asList(Budget.builder().id(1L).build(), Budget.builder().id(2L).build());
        Page<Budget> budgetPage = new PageImpl<>(budgetList);

        when(budgetRepository.findAll(pageable)).thenReturn(budgetPage);

        Page<Budget> result = budgetService.list(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }
}
