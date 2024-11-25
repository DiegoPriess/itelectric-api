package com.iteletric.iteletricapi.repositories;

import com.iteletric.iteletricapi.models.Budget;
import com.iteletric.iteletricapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT budget FROM Budget budget WHERE budget.owner = :owner AND " +
            "(:customerEmail IS NULL OR LOWER(budget.customer.email) LIKE LOWER(CONCAT('%', :customerEmail, '%')))")
    Page<Budget> findByOwnerAndCustomerEmail(@Param("owner") User owner, @Param("customerEmail") String customerEmail, Pageable pageable);
    Page<Budget> findByOwner(User owner, Pageable pageable);
    Page<Budget> findByCustomer(User customer, Pageable pageable);
}
