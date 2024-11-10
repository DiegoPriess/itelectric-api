package com.iteletric.iteletricapi.repositories;

import com.iteletric.iteletricapi.models.Material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Page<Material> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
