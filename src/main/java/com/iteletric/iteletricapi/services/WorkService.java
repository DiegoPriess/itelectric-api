package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.dtos.work.WorkResponse;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WorkService {

    private final WorkRepository repository;
    private final BudgetRepository budgetRepository;
    private final MaterialService materialService;
    private final UserService userService;

    private static final String WORK_NOT_FOUND_MESSAGE = "Trabalho não encontrado";

    @Autowired
    public WorkService(WorkRepository repository, MaterialService materialService, UserService userService, BudgetRepository budgetRepository) {
        this.repository = repository;
        this.materialService = materialService;
        this.userService = userService;
        this.budgetRepository = budgetRepository;
    }

    public void create(WorkRequest workRequest) {
        List<Material> materialList = materialService.getAllMaterialSelectedById(workRequest.getMaterialIdList());

        Work work = new Work();
        work.setName(workRequest.getName());
        work.setLaborPrice(workRequest.getLaborPrice());
        work.setMaterialList(materialList);

        repository.save(work);
    }

    public void update(Long workId, WorkRequest workRequest) {
        Work work = getById(workId);
        List<Material> materialList = materialService.getAllMaterialSelectedById(workRequest.getMaterialIdList());

        work.setName(workRequest.getName());
        work.setLaborPrice(workRequest.getLaborPrice());
        work.setMaterialList(materialList);
        repository.save(work);

        recalculateBudgetTotalValue(work);
    }

    public void delete(Long workId) {
        Work work = repository.findById(workId)
                              .orElseThrow(() -> new BusinessException(WORK_NOT_FOUND_MESSAGE));

        try {
            repository.delete(work);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Não foi possível excluir trabalho, pois ele está associado a um orçamento!");
        }
    }

    public Work getById(Long workId) {
        return repository.findById(workId)
                         .orElseThrow(() -> new BusinessException(WORK_NOT_FOUND_MESSAGE));
    }

    public Page<WorkResponse> list(String name, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        final User currentUser = userService.getCurrentUser();

        if (name != null && !name.isEmpty()) return WorkResponse.convert(repository.findByOwnerAndNameContainingIgnoreCase(currentUser, name, sortedPageable));
        return WorkResponse.convert(repository.findByOwner(currentUser, sortedPageable));
    }

    public List<Work> getAllWorkSelectedById(List<Long> workIdList) {
        List<Work> workList = repository.findAllById(workIdList);
        if (workList.isEmpty()) throw new BusinessException("Os serviços selecionados não foram encontrados");
        return workList;
    }

    private void recalculateBudgetTotalValue(Work work) {
        List<Budget> budgetsList = budgetRepository.findByWorkListContaining(work);
        if (budgetsList.isEmpty()) return;

        for(Budget budget: budgetsList) {
            BigDecimal newTotalValue = budget.calculateTotalValue();
            budget.setTotalValue(newTotalValue);
            budgetRepository.save(budget);
        }
    }
}
