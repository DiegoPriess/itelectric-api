package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequestDTO;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkService {

    private final WorkRepository repository;
    private final MaterialService materialService;

    @Autowired
    public WorkService(WorkRepository repository, MaterialService materialService) {
        this.repository = repository;
        this.materialService = materialService;
    }

    public Work create(WorkRequestDTO workRequestDTO) {
        List<Material> materialList = materialService.getAllMaterialSelectedById(workRequestDTO.getMaterialIdList());

        Work work = new Work();
        work.setName(workRequestDTO.getName());
        work.setPrice(workRequestDTO.getPrice());
        work.setMaterialList(materialList);

        return repository.save(work);
    }

    public Work update(Long workId, WorkRequestDTO workRequestDTO) {
        Work work = getById(workId);

        List<Material> materialList = materialService.getAllMaterialSelectedById(workRequestDTO.getMaterialIdList());

        work.setName(workRequestDTO.getName());
        work.setPrice(workRequestDTO.getPrice());
        work.setMaterialList(materialList);

        return repository.save(work);
    }

    public void delete(Long workId) {
        Work work = repository.findById(workId)
                              .orElseThrow(() -> new BusinessException("Serviço não encontrado"));

        repository.delete(work);
    }

    public Work getById(Long workId) {
        return repository.findById(workId)
                         .orElseThrow(() -> new BusinessException("Serviço não encontrado"));
    }

    public Page<Work> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Work> getAllWorkSelectedById(List<Long> workIdList) {
        List<Work> workList = repository.findAllById(workIdList);
        if (workList.isEmpty()) throw new BusinessException("Os serviços selecionados não foram encontrados");
        return workList;
    }
}
