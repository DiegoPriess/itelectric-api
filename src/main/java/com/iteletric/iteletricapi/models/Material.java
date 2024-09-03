package com.iteletric.iteletricapi.models;

import com.iteletric.iteletricapi.config.base_entities.BaseModel;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @Column(name="unit_measure", nullable = false)
    private UnitOfMeasure unitMeasure;

    @Column(name="quantity_unit_measure", nullable = false)
    private BigDecimal quantityUnitMeasure;

}
