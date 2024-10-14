package com.iteletric.iteletricapi.models;

import com.iteletric.iteletricapi.config.baseentities.BaseModel;
import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "material")
public class Material extends BaseModel {

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @Column(name="unit_measure", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitMeasure;

    @Column(name="quantity_unit_measure", nullable = false)
    private BigDecimal quantityUnitMeasure;

}
