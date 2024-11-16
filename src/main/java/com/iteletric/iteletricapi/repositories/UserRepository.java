package com.iteletric.iteletricapi.repositories;

import com.iteletric.iteletricapi.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT user FROM User user JOIN user.roles role WHERE role.name = :role")
    Page<User> findByRoleName(@Param("role") String role, Pageable pageable);
}
