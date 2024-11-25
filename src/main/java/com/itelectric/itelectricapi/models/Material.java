package com.itelectric.itelectricapi.models;

import com.itelectric.itelectricapi.config.baseentities.BaseModel;
import com.itelectric.itelectricapi.enums.material.UnitOfMeasure;
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
    private UnitOfMeasure unitMeasure;

    @Column(name="quantity_unit_measure", nullable = false)
    private BigDecimal quantityUnitMeasure;

}
