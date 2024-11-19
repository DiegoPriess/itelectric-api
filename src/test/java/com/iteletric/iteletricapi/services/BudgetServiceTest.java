package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequest;
import com.iteletric.iteletricapi.enums.budget.BudgetStatus;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private WorkService workService;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;

    @InjectMocks
    private BudgetService budgetService;

    private BudgetRequest budgetRequest;
    private User user;
    private Work work;
    private Budget budget;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("diegopriess.dev@gmail.com");
        List<Work> workList = Collections.singletonList(work);

        budgetRequest = new BudgetRequest();
        budgetRequest.setCustomerEmail("diegopriess.dev@gmail.com");
        budgetRequest.setWorkIdList(List.of(1L));
        budgetRequest.setDeliveryForecast(LocalDate.now());

        budget = new Budget();
        budget.setWorkList(workList);
        budget.setCustomer(user);
        budget.setDeliveryForecast(budgetRequest.getDeliveryForecast());
        budget.setStatus(BudgetStatus.PENDING);
    }

    @Test
    void createBudget_ShouldCreateBudgetWhenRequestIsValid() {
        when(userService.createCustomerIfNecessary(anyString())).thenReturn(user);
        when(workService.getAllWorkSelectedById(anyList())).thenReturn(budget.getWorkList());

        budgetService.create(budgetRequest);

        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void createBudget_ShouldThrowExceptionWhenWorkListIsEmpty() {
        budgetRequest.setWorkIdList(List.of());

        Exception exception = assertThrows(BusinessException.class, () -> {
            budgetService.create(budgetRequest);
        });

        assertEquals("Para criar um orçamento, é necessário selecionar pelo menos um trabalho", exception.getMessage());
    }

    @Test
    void deleteBudget_ShouldDeleteBudgetWhenFound() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.of(budget));
        budgetService.delete(1L);
        verify(budgetRepository).delete(budget);
    }

    @Test
    void deleteBudget_ShouldThrowExceptionWhenNotFound() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        Exception exception = assertThrows(BusinessException.class, () -> {
            budgetService.delete(1L);
        });

        assertEquals("Orçamento não encontrado", exception.getMessage());
    }

    @Test
    void updateBudget_ShouldUpdateBudgetWhenRequestIsValid() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.of(budget));
        when(userService.createCustomerIfNecessary(anyString())).thenReturn(user);
        when(workService.getAllWorkSelectedById(anyList())).thenReturn(budget.getWorkList());
        budgetService.update(1L, budgetRequest);
        verify(budgetRepository).save(budget);
        assertEquals(BudgetStatus.PENDING, budget.getStatus());
        assertEquals("diegopriess.dev@gmail.com", budget.getCustomer().getEmail());
    }

    @Test
    void updateBudget_ShouldThrowExceptionWhenBudgetNotFound() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        Exception exception = assertThrows(BusinessException.class, () -> {
            budgetService.update(1L, budgetRequest);
        });
        assertEquals("Orçamento não encontrado", exception.getMessage());
    }

    @Test
    void approveBudget_ShouldSetStatusToApproved() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.of(budget));
        budgetService.approve(1L);
        assertEquals(BudgetStatus.APPROVED, budget.getStatus());
        verify(budgetRepository).save(budget);
    }

    @Test
    void approveBudget_ShouldThrowExceptionWhenBudgetNotFound() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        Exception exception = assertThrows(BusinessException.class, () -> {
            budgetService.approve(1L);
        });
        assertEquals("Orçamento não encontrado", exception.getMessage());
    }

    @Test
    void denyBudget_ShouldSetStatusToDenied() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.of(budget));
        budgetService.deny(1L);
        assertEquals(BudgetStatus.DENIED, budget.getStatus());
        verify(budgetRepository).save(budget);
    }

    @Test
    void denyBudget_ShouldThrowExceptionWhenBudgetNotFound() {
        when(budgetRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        Exception exception = assertThrows(BusinessException.class, () -> {
            budgetService.deny(1L);
        });
        assertEquals("Orçamento não encontrado", exception.getMessage());
    }
}
