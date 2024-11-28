package com.iteletric.iteletricapi.models;

import com.iteletric.iteletricapi.config.baseentities.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "work")
public class Work extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="labor_price", nullable = false)
    private BigDecimal laborPrice;

    @Column(name="material_price")
    private BigDecimal materialPrice;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="work_material",
               joinColumns = @JoinColumn(name = "work_id"),
               inverseJoinColumns = @JoinColumn(name="material_id"))
    private List<Material> materialList;

    public BigDecimal calculateMaterialPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (Material material : materialList) {
            total = total.add(material.getPrice());
        }

        return total;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.materialPrice = calculateMaterialPrice();
    }
}
