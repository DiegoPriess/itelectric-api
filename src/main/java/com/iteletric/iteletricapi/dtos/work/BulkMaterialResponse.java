package com.iteletric.iteletricapi.dtos.work;

import com.iteletric.iteletricapi.dtos.enums.EnumDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkMaterialResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private EnumDTO unitMeasure;

    private Integer quantityUnitMeasure;
}
