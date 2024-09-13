package com.iteletric.iteletricapi.repositories;

import com.iteletric.iteletricapi.models.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
}
