package com.iteletric.iteletricapi.dtos.material;

import com.iteletric.iteletricapi.dtos.UnitOfMeasureDTO;
import com.iteletric.iteletricapi.models.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class MaterialResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private UnitOfMeasureDTO unitMeasure;
    private BigDecimal quantityUnitMeasure;

    public static MaterialResponse convert(Material material) {
        return MaterialResponse.builder().id(material.getId())
                .name(material.getName())
                .price(material.getPrice())
                .unitMeasure(new UnitOfMeasureDTO(material.getUnitMeasure().toString(), material.getUnitMeasure().getLabel()))
                .quantityUnitMeasure(material.getQuantityUnitMeasure())
                .build();
    }

    public static Page<MaterialResponse> convert(Page<Material> materialPage) {
        return materialPage.map(material ->
                MaterialResponse.builder()
                        .id(material.getId())
                        .name(material.getName())
                        .price(material.getPrice())
                        .unitMeasure(new UnitOfMeasureDTO(material.getUnitMeasure().toString(), material.getUnitMeasure().getLabel()))
                        .quantityUnitMeasure(material.getQuantityUnitMeasure())
                        .build()
        );
    }
}