package com.iteletric.iteletricapi.models;

import com.iteletric.iteletricapi.config.base_entities.BaseModel;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="budget_work",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name="work_id"))
    private List<Work> workList;

    @Column(name = "delivery_forecast", nullable = false)
    private LocalDate deliveryForecast;

    @Transient
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

    @PostLoad
    @PostPersist
    @PostUpdate
    public void updateTotalValue() {
        this.totalValue = calculateTotalValue();
    }
}
