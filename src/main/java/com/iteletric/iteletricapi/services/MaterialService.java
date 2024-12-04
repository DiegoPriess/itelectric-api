package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.material.MaterialResponse;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.MaterialRepository;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    MaterialService(MaterialRepository repository, UserService userService, WorkRepository workRepository) {
        this.repository = repository;
        this.userService = userService;
        this.workRepository = workRepository;
    }

    private final MaterialRepository repository;
    private final WorkRepository workRepository;
    private final UserService userService;

    private static final String materialNotFoundMessage = "Material não encontrado";

    public void create(Material material) {
        repository.save(material);
    }

    public void update(Long materialId, Material materialDetails) {
        Material material = repository.findById(materialId)
                                      .orElseThrow(() -> new BusinessException(materialNotFoundMessage));

        material.setName(materialDetails.getName());
        material.setPrice(materialDetails.getPrice());
        material.setUnitMeasure(materialDetails.getUnitMeasure());
        material.setQuantityUnitMeasure(materialDetails.getQuantityUnitMeasure());

        repository.save(material);

        recalculateWorkMaterialValue(material);
    }

    public void delete(Long materialId) {
        Material material = repository.findById(materialId)
                                      .orElseThrow(() -> new BusinessException(materialNotFoundMessage));

        try {
            repository.delete(material);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Não foi possível excluir o material, pois ele está associado a um trabalho existente!");
        }
    }

    public MaterialResponse getById(Long materialId) {
        Material material = repository.findById(materialId)
                                      .orElseThrow(() -> new BusinessException(materialNotFoundMessage));

        return MaterialResponse.convert(material);
    }

    public Page<MaterialResponse> list(String name, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        final User currentUser = userService.getCurrentUser();

        if (name != null && !name.isEmpty()) return MaterialResponse.convert(repository.findByOwnerAndNameContainingIgnoreCase(currentUser, name, sortedPageable));
        return MaterialResponse.convert(repository.findByOwner(currentUser, sortedPageable));
    }

    public List<Material> getAllMaterialSelectedById(List<Long> materialIdList) {
        if (materialIdList.isEmpty()) return new ArrayList<>();
        return repository.findAllById(materialIdList);
    }

    private void recalculateWorkMaterialValue(Material material) {
        List<Work> workList = workRepository.findByMaterialListContaining(material);
        if (workList.isEmpty()) return;

        for(Work work: workList) {
            BigDecimal newMaterialPrice = work.calculateMaterialPrice();
            work.setMaterialPrice(newMaterialPrice);
            workRepository.save(work);
        }
    }
}
