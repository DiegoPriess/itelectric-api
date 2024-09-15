package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    private final MaterialRepository repository;

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

    public Material getById(Long materialId) {
        return repository.findById(materialId)
                         .orElseThrow(() -> new BusinessException("Material não encontrado"));
    }

    public Page<Material> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Material> getAllMaterialSelectedById(List<Long> workIdList) {
        List<Material> materialList = repository.findAllById(workIdList);
        if (materialList.isEmpty()) throw new BusinessException("Os materiais selecionados não foram encontrados");
        return materialList;
    }
}
