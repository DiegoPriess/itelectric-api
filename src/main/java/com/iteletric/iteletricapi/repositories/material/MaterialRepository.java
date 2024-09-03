package com.iteletric.iteletricapi.repositories.material;

import com.iteletric.iteletricapi.models.Material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
