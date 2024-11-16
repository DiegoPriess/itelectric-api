package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.dtos.work.WorkResponse;
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
    private final UserService userService;

    @Autowired
    public WorkService(WorkRepository repository, MaterialService materialService, UserService userService) {
        this.repository = repository;
        this.materialService = materialService;
        this.userService = userService;
    }

    public Work create(WorkRequest workRequest) {
        List<Material> materialList = materialService.getAllMaterialSelectedById(workRequest.getMaterialIdList());

        Work work = new Work();
        work.setName(workRequest.getName());
        work.setPrice(workRequest.getPrice());
        work.setMaterialList(materialList);

        return repository.save(work);
    }

    public Work update(Long workId, WorkRequest workRequest) {
        Work work = getById(workId);

        List<Material> materialList = materialService.getAllMaterialSelectedById(workRequest.getMaterialIdList());

        work.setName(workRequest.getName());
        work.setPrice(workRequest.getPrice());
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

    public Page<WorkResponse> list(String name, Pageable pageable) {
        final Long currentUserId = userService.getCurrentUserId();

        if (name != null && !name.isEmpty()) return WorkResponse.convert(repository.findByOwnerAndNameContainingIgnoreCase(currentUserId, name, pageable));
        return WorkResponse.convert(repository.findByOwner(currentUserId, pageable));
    }

    public List<Work> getAllWorkSelectedById(List<Long> workIdList) {
        List<Work> workList = repository.findAllById(workIdList);
        if (workList.isEmpty()) throw new BusinessException("Os serviços selecionados não foram encontrados");
        return workList;
    }
}
