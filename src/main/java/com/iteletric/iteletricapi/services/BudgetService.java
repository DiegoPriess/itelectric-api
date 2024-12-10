package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequest;
import com.iteletric.iteletricapi.enums.budget.BudgetStatus;
import com.iteletric.iteletricapi.enums.user.RoleName;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository repository;
    private final WorkService workService;
    private final UserService userService;
    private final MailService mailService;

    private static final String BUDGET_NOT_FOUND_MESSAGE = "Orçamento não encontrado";

    @Autowired
    public BudgetService(BudgetRepository repository, WorkService workService, UserService userService, MailService mailService) {
        this.repository = repository;
        this.workService = workService;
        this.userService = userService;
        this.mailService = mailService;
    }

    public void create(BudgetRequest budgetRequest) {
        if (budgetRequest.getWorkIdList().isEmpty()) throw new BusinessException("Para criar um orçamento, é necessário selecionar pelo menos um trabalho");

        User customer = userService.createCustomerIfNecessary(budgetRequest.getCustomerEmail());

        if (RoleName.ROLE_OWNER.equals(customer.getRole())) throw new BusinessException("Não é possível criar orçamento para um cliente já cadastrado como eletrecista");

        List<Work> workList = workService.getAllWorkSelectedById(budgetRequest.getWorkIdList());

        Budget budget = Budget.builder()
                .workList(workList)
                .customer(customer)
                .deliveryForecast(budgetRequest.getDeliveryForecast())
                .status(BudgetStatus.PENDING)
                .build();

        repository.save(budget);

        this.mailService.sendHtml(customer.getEmail(), "Novo orçamento criado para você, venha conferir!", this.mailService.createNewBudgetEmail());
    }

    public void update(Long budgetId, BudgetRequest budgetRequest) {
        if (budgetRequest.getWorkIdList().isEmpty()) throw new BusinessException("Para alterar um orçamento, é necessário manter pelo menos um trabalho");

        Budget budget = getById(budgetId);
        User customer = userService.createCustomerIfNecessary(budgetRequest.getCustomerEmail());
        List<Work> workList = workService.getAllWorkSelectedById(budgetRequest.getWorkIdList());

        budget.setWorkList(workList);
        budget.setCustomer(customer);
        budget.setDeliveryForecast(budgetRequest.getDeliveryForecast());

        repository.save(budget);
    }

    public void delete(Long userId) {
        Budget budget = repository.findById(userId)
                .orElseThrow(() -> new BusinessException(BUDGET_NOT_FOUND_MESSAGE));

        repository.delete(budget);
    }

    public Budget getById(Long budgetId) {
        return repository.findById(budgetId)
                .orElseThrow(() -> new BusinessException(BUDGET_NOT_FOUND_MESSAGE));
    }

    public Page<Budget> list(String customerEmail, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        final User currentUser = userService.getCurrentUser();

        if (customerEmail != null && !customerEmail.isEmpty()) return repository.findByOwnerAndCustomerEmail(currentUser, customerEmail, sortedPageable);
        return repository.findByOwner(currentUser, sortedPageable);
    }

    public Page<Budget> customerList(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        final User currentUser = userService.getCurrentUser();
        return repository.findByCustomer(currentUser, sortedPageable);
    }

    public void approve(Long budgetId) {
        Budget budget = repository.findById(budgetId)
                .orElseThrow(() -> new BusinessException(BUDGET_NOT_FOUND_MESSAGE));

        budget.setStatus(BudgetStatus.APPROVED);
        repository.save(budget);
    }

    public void deny(Long budgetId) {
        Budget budget = repository.findById(budgetId)
                .orElseThrow(() -> new BusinessException(BUDGET_NOT_FOUND_MESSAGE));

        budget.setStatus(BudgetStatus.DENIED);
        repository.save(budget);
    }
}
