package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequest;
import com.iteletric.iteletricapi.enums.budget.BudgetStatus;
import com.iteletric.iteletricapi.enums.user.RoleName;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Test
    void listCustomerBudgets_ShouldReturnCustomerBudgets() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")); // Adicione o sort correto
        User currentUser = new User();
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(budgetRepository.findByCustomer(currentUser, pageable)).thenReturn(Page.empty());

        Page<Budget> result = budgetService.customerList(pageable);

        assertEquals(0, result.getTotalElements());
        verify(budgetRepository).findByCustomer(currentUser, pageable);
    }

    @Test
    void listBudgets_ShouldReturnBudgetsFilteredByEmail() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        User currentUser = new User();
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(budgetRepository.findByOwnerAndCustomerEmail(currentUser, "diegopriess.dev@gmail.com", pageable))
                .thenReturn(Page.empty());

        Page<Budget> result = budgetService.list("diegopriess.dev@gmail.com", pageable);

        assertEquals(0, result.getTotalElements());
        verify(budgetRepository).findByOwnerAndCustomerEmail(currentUser, "diegopriess.dev@gmail.com", pageable);
    }


    @Test
    void listBudgets_ShouldReturnBudgetsWithoutFilter() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        User currentUser = new User();
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(budgetRepository.findByOwner(currentUser, pageable)).thenReturn(Page.empty());

        Page<Budget> result = budgetService.list(null, pageable);

        assertEquals(0, result.getTotalElements());
        verify(budgetRepository).findByOwner(currentUser, pageable);
    }

    @Test
    void createBudget_ShouldThrowExceptionWhenCustomerIsOwner() {
        User owner = new User();
        owner.setRole(RoleName.ROLE_OWNER);
        when(userService.createCustomerIfNecessary(anyString())).thenReturn(owner);

        Exception exception = assertThrows(BusinessException.class, () -> {
            budgetService.create(budgetRequest);
        });

        assertEquals("Não é possível criar orçamento para um cliente já cadastrado como eletrecista", exception.getMessage());
    }

    @Test
    void updateBudget_ShouldThrowExceptionWhenWorkListIsEmpty() {
        budgetRequest.setWorkIdList(Collections.emptyList());

        Exception exception = assertThrows(BusinessException.class, () -> {
            budgetService.update(1L, budgetRequest);
        });

        assertEquals("Para alterar um orçamento, é necessário manter pelo menos um trabalho", exception.getMessage());
    }
}
