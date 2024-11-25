package com.itelectric.itelectricapi.service;

import com.itelectric.itelectricapi.config.exception.BusinessException;
import com.itelectric.itelectricapi.dtos.work.WorkRequest;
import com.itelectric.itelectricapi.dtos.work.WorkResponse;
import com.itelectric.itelectricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.itelectric.itelectricapi.models.Work;
import com.itelectric.itelectricapi.repositories.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if (workRequest.getMaterialIdList().isEmpty()) throw new BusinessException("Para criar um trabalho, é necessário selecionar pelo menos um material");

        List<Material> materialList = materialService.getAllMaterialSelectedById(workRequest.getMaterialIdList());

        Work work = new Work();
        work.setName(workRequest.getName());
        work.setPrice(workRequest.getPrice());
        work.setMaterialList(materialList);

        return repository.save(work);
    }

    public Work update(Long workId, WorkRequest workRequest) {
        if (workRequest.getMaterialIdList().isEmpty()) throw new BusinessException("Para alterar um trabalho, é necessário manter pelo menos um material");

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
}
