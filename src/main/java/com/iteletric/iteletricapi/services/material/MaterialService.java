package com.iteletric.iteletricapi.services.material;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.material.MaterialRepository;
import com.iteletric.iteletricapi.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repositorys;

    @Autowired
    private UserRepository repository;

    public void create(Material material) {
        repositorys.save(material);
    }

    public User getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }

    public void update(Long id, Material materialDetails) {
        Material material = repositorys.findById(id)
                                      .orElseThrow(() -> new BusinessException("Material não encontrado"));

        material.setName(materialDetails.getName());
        material.setPrice(materialDetails.getPrice());
        material.setUnitMeasure(materialDetails.getUnitMeasure());
        material.setQuantityUnitMeasure(materialDetails.getQuantityUnitMeasure());

        repositorys.save(material);
    }

    public void delete(Long id) {
        Material material = repositorys.findById(id)
                                      .orElseThrow(() -> new BusinessException("Material não encontrado"));

        repositorys.delete(material);
    }

    public Page<Material> list(Pageable pageable) {
        return repositorys.findAll(pageable);
    }
}
