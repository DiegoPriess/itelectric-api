package com.iteletric.iteletricapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iteletric.iteletricapi.config.baseentities.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bulk_material")
public class BulkMaterial extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_id")
    @JsonIgnore
    private Work work;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "bulk_quantity", nullable = false)
    private Integer bulkQuantity;

    public BigDecimal getPrice() {
        if (material != null && material.getPrice() != null) {
            return material.getPrice()
                    .divide(BigDecimal.valueOf(material.getQuantityUnitMeasure()), RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(this.bulkQuantity));
        }
        return BigDecimal.ZERO;
    }
}
