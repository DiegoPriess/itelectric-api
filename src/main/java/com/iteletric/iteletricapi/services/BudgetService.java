package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.budget.BudgetRequestDTO;
import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository repository;
    private final WorkRepository workRepository;

    @Autowired
    public BudgetService(BudgetRepository repository, WorkRepository workRepository) {
        this.repository = repository;
        this.workRepository = workRepository;
    }

    public Budget create(BudgetRequestDTO budgetRequestDTO) {
        List<Work> works = workRepository.findAllById(budgetRequestDTO.getWorkIdList());
        if (works.isEmpty()) {
            throw new BusinessException("Nenhum trabalho encontrado com os IDs fornecidos");
        }

        Budget budget = Budget.builder()
                .workList(works)
                .deliveryForecast(budgetRequestDTO.getDeliveryForecast())
                .build();

        return repository.save(budget);
    }

    public Budget update(Long id, BudgetRequestDTO budgetRequestDTO) {
        Budget budget = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));

        List<Work> works = workRepository.findAllById(budgetRequestDTO.getWorkIdList());
        if (works.isEmpty()) {
            throw new BusinessException("Nenhum trabalho encontrado com os IDs fornecidos");
        }

        budget.setWorkList(works);
        budget.setDeliveryForecast(budgetRequestDTO.getDeliveryForecast());

        return repository.save(budget);
    }

    public void delete(Long id) {
        Budget budget = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));

        repository.delete(budget);
    }

    public Budget getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Orçamento não encontrado"));
    }

    public Page<Budget> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
