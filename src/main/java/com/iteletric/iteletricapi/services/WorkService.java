package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequestDTO;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.models.material.Material;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import com.iteletric.iteletricapi.repositories.material.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkService {

    private final WorkRepository repository;
    private final MaterialRepository materialRepository;

    @Autowired
    public WorkService(WorkRepository repository, MaterialRepository materialRepository) {
        this.repository = repository;
        this.materialRepository = materialRepository;
    }

    public Work create(WorkRequestDTO workRequestDTO) {
        List<Material> materials = materialRepository.findAllById(workRequestDTO.getMaterialIdList());
        if (materials.isEmpty()) {
            throw new BusinessException("Nenhum material encontrado com os IDs fornecidos");
        }

        Work work = new Work();
        work.setName(workRequestDTO.getName());
        work.setPrice(workRequestDTO.getPrice());
        work.setMaterialList(materials);

        return repository.save(work);
    }

    public Work update(Long id, WorkRequestDTO workRequestDTO) {
        Work work = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Trabalho não encontrado"));

        List<Material> materials = materialRepository.findAllById(workRequestDTO.getMaterialIdList());
        if (materials.isEmpty()) {
            throw new BusinessException("Nenhum material encontrado com os IDs fornecidos");
        }

        work.setName(workRequestDTO.getName());
        work.setPrice(workRequestDTO.getPrice());
        work.setMaterialList(materials);

        return repository.save(work);
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
