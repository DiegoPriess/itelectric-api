package com.iteletric.iteletricapi.models;

import com.iteletric.iteletricapi.config.baseentities.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "budget")
public class Budget extends BaseModel {

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "budget_work",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id"))
    private List<Work> workList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "delivery_forecast", nullable = false)
    private LocalDate deliveryForecast;

    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;

    public BigDecimal calculateTotalValue() {
        BigDecimal total = BigDecimal.ZERO;

        for (Work work : workList) {
            BigDecimal workPrice = work.getPrice();
            total = total.add(workPrice);
            for (Material material : work.getMaterialList()) {
                total = total.add(material.getPrice());
            }
        }

        return total;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.totalValue = calculateTotalValue();
    }
}
