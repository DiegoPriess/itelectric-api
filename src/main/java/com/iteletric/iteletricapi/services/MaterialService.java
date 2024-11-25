package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.material.MaterialResponse;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    MaterialService(MaterialRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    private final MaterialRepository repository;

    private final UserService userService;

    public void create(Material material) {
        repository.save(material);
    }

    public void update(Long materialId, Material materialDetails) {
        Material material = repository.findById(materialId)
                                      .orElseThrow(() -> new BusinessException("Material não encontrado"));

        material.setName(materialDetails.getName());
        material.setPrice(materialDetails.getPrice());
        material.setUnitMeasure(materialDetails.getUnitMeasure());
        material.setQuantityUnitMeasure(materialDetails.getQuantityUnitMeasure());

        repository.save(material);
    }

    public void delete(Long materialId) {
        Material material = repository.findById(materialId)
                                      .orElseThrow(() -> new BusinessException("Material não encontrado"));

        repository.delete(material);
    }

    public MaterialResponse getById(Long materialId) {
        Material material = repository.findById(materialId)
                                      .orElseThrow(() -> new BusinessException("Material não encontrado"));

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

    public List<Material> getAllMaterialSelectedById(List<Long> workIdList) {
        List<Material> materialList = repository.findAllById(workIdList);
        if (materialList.isEmpty()) throw new BusinessException("Os materiais selecionados não foram encontrados");
        return materialList;
    }
}
