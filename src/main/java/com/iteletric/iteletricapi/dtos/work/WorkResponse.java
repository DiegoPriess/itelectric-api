package com.iteletric.iteletricapi.dtos.work;

import com.iteletric.iteletricapi.dtos.enums.EnumDTO;
import com.iteletric.iteletricapi.dtos.material.MaterialResponse;
import com.iteletric.iteletricapi.models.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkResponse {

    private Long id;

    private String name;

    private BigDecimal laborPrice;

    private BigDecimal materialPrice;

    private List<MaterialResponse> materialList;

    public static Page<WorkResponse> convert(Page<Work> workPage) {
        return workPage.map(work ->
                WorkResponse.builder()
                        .id(work.getId())
                        .name(work.getName())
                        .laborPrice(work.getLaborPrice())
                        .materialPrice(work.getMaterialPrice())
                        .materialList(
                                work.getMaterialList().stream().map(material ->
                                        MaterialResponse.builder()
                                                .id(material.getId())
                                                .name(material.getName())
                                                .price(material.getPrice())
                                                .unitMeasure(new EnumDTO(material.getUnitMeasure().toString(), material.getUnitMeasure().getLabel()))
                                                .quantityUnitMeasure(material.getQuantityUnitMeasure())
                                                .build()
                                ).toList()
                        )
                        .build()
        );
    }

    public static List<WorkResponse> convert(List<Work> workList) {
        return workList.stream().map(work ->
                WorkResponse.builder()
                        .id(work.getId())
                        .name(work.getName())
                        .laborPrice(work.getLaborPrice())
                        .materialPrice(work.getMaterialPrice())
                        .materialList(
                                work.getMaterialList().stream().map(material ->
                                        MaterialResponse.builder()
                                                .id(material.getId())
                                                .name(material.getName())
                                                .price(material.getPrice())
                                                .unitMeasure(new EnumDTO(
                                                        material.getUnitMeasure().toString(),
                                                        material.getUnitMeasure().getLabel()
                                                ))
                                                .quantityUnitMeasure(material.getQuantityUnitMeasure())
                                                .build()
                                ).collect(Collectors.toList())
                        )
                        .build()
        ).collect(Collectors.toList());
    }

}
