package com.iteletric.iteletricapi.repositories;

import com.iteletric.iteletricapi.enums.user.RoleName;
import com.iteletric.iteletricapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByRole(RoleName role, Pageable pageable);
}
