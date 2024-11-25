package com.itelectric.itelectricapi.repositories;

import com.iteletric.iteletricapi.models.User;
import com.itelectric.itelectricapi.models.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    Page<Work> findByOwnerAndNameContainingIgnoreCase(User owner, String name, Pageable pageable);

    Page<Work> findByOwner(User owner, Pageable pageable);
}
