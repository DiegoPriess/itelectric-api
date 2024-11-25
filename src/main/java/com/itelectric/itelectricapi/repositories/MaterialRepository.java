package com.itelectric.itelectricapi.repositories;

import com.itelectric.itelectricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Page<Material> findByOwnerAndNameContainingIgnoreCase(User owner, String name, Pageable pageable);

    Page<Material> findByOwner(User owner, Pageable pageable);
}
