package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequestDTO;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private WorkService workService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBudgetSuccess() {
        BudgetRequestDTO requestDTO = new BudgetRequestDTO();
        requestDTO.setWorkIdList(Arrays.asList(1L, 2L));
        requestDTO.setCustomerId(1L);
        requestDTO.setDeliveryForecast(LocalDate.now());

        List<Work> workList = Arrays.asList(new Work(), new Work());
        User customer = new User();

        when(workService.getAllWorkSelectedById(anyList())).thenReturn(workList);
        when(userService.getUserById(anyLong())).thenReturn(customer);

        Budget savedBudget = Budget.builder()
                .workList(workList)
                .customer(customer)
                .deliveryForecast(requestDTO.getDeliveryForecast())
                .build();

        when(budgetRepository.save(any(Budget.class))).thenReturn(savedBudget);

        Budget result = budgetService.create(requestDTO);

        assertNotNull(result);
        assertEquals(savedBudget, result);
        verify(budgetRepository, times(1)).save(any(Budget.class));
        verify(workService, times(1)).getAllWorkSelectedById(requestDTO.getWorkIdList());
        verify(userService, times(1)).getUserById(requestDTO.getCustomerId());
    }

    @Test
    void updateBudgetSuccess() {
        Long budgetId = 1L;
        BudgetRequestDTO requestDTO = new BudgetRequestDTO();
        requestDTO.setWorkIdList(Arrays.asList(1L, 2L));
        requestDTO.setCustomerId(1L);
        requestDTO.setDeliveryForecast(LocalDate.now());

        Budget existingBudget = new Budget();
        List<Work> workList = Arrays.asList(new Work(), new Work());
        User customer = new User();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(existingBudget));
        when(workService.getAllWorkSelectedById(anyList())).thenReturn(workList);
        when(userService.getUserById(anyLong())).thenReturn(customer);
        when(budgetRepository.save(any(Budget.class))).thenReturn(existingBudget);

        Budget result = budgetService.update(budgetId, requestDTO);

        assertNotNull(result);
        verify(budgetRepository, times(1)).save(existingBudget);
        verify(workService, times(1)).getAllWorkSelectedById(requestDTO.getWorkIdList());
        verify(userService, times(1)).getUserById(requestDTO.getCustomerId());
    }

    @Test
    void deleteBudgetSuccess() {
        Long budgetId = 1L;
        Budget existingBudget = new Budget();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(existingBudget));

        budgetService.delete(budgetId);

        verify(budgetRepository, times(1)).delete(existingBudget);
    }

    @Test
    void deleteBudgetNotFound() {
        Long budgetId = 1L;

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> budgetService.delete(budgetId));
    }

    @Test
    void getByIdBudgetSuccess() {
        Long budgetId = 1L;
        Budget budget = new Budget();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        Budget result = budgetService.getById(budgetId);

        assertNotNull(result);
        verify(budgetRepository, times(1)).findById(budgetId);
    }

    @Test
    void getByIdBudgetNotFound() {
        Long budgetId = 1L;

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> budgetService.getById(budgetId));
    }

    @Test
    void listBudgetsSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Budget budget = new Budget();
        Page<Budget> page = new PageImpl<>(List.of(budget));

        when(budgetRepository.findAll(pageable)).thenReturn(page);

        Page<Budget> result = budgetService.list(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(budgetRepository, times(1)).findAll(pageable);
    }
}
