package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WorkService {

    private final WorkRepository repository;

    @Autowired
    WorkService(WorkRepository repository) {
        this.repository = repository;
    }

    public void create(Work work) {
        repository.save(work);
    }

    public void update(Long id, Work workDetails) {
        Work work = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Trabalho não encontrado"));

        work.setName(workDetails.getName());
        work.setPrice(workDetails.getPrice());
        work.setMaterials(workDetails.getMaterials());

        repository.save(work);
    }

    public void delete(Long id) {
        Work work = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Trabalho não encontrado"));

        repository.delete(work);
    }

    public Work getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Trabalho não encontrado"));
    }

    public Page<Work> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
