package com.iteletric.iteletricapi.dtos.work;

import com.iteletric.iteletricapi.dtos.enums.EnumDTO;
import com.iteletric.iteletricapi.models.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkResponse {

    private Long id;

    private String name;

    private BigDecimal laborPrice;

    private BigDecimal materialPrice;

    private List<BulkMaterialResponse> materialList;

    public static Page<WorkResponse> convert(Page<Work> workPage) {
        return workPage.map(work ->
                WorkResponse.builder()
                        .id(work.getId())
                        .name(work.getName())
                        .laborPrice(work.getLaborPrice())
                        .materialPrice(work.getMaterialPrice())
                        .materialList(
                                work.getMaterialList().stream().map(workMaterial ->
                                        BulkMaterialResponse.builder()
                                                .id(workMaterial.getMaterial().getId())
                                                .name(workMaterial.getMaterial().getName())
                                                .price(workMaterial.getPrice())
                                                .unitMeasure(new EnumDTO(
                                                        workMaterial.getMaterial().getUnitMeasure().toString(),
                                                        workMaterial.getMaterial().getUnitMeasure().getLabel()
                                                ))
                                                .quantityUnitMeasure(workMaterial.getBulkQuantity())
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
                                work.getMaterialList().stream().map(workMaterial ->
                                        BulkMaterialResponse.builder()
                                                .id(workMaterial.getMaterial().getId())
                                                .name(workMaterial.getMaterial().getName())
                                                .price(workMaterial.getPrice())
                                                .unitMeasure(new EnumDTO(
                                                        workMaterial.getMaterial().getUnitMeasure().toString(),
                                                        workMaterial.getMaterial().getUnitMeasure().getLabel()
                                                ))
                                                .quantityUnitMeasure(workMaterial.getBulkQuantity())
                                                .build()
                                ).toList()
                        )
                        .build()
        ).toList();
    }

    public static WorkResponse convert(Work work) {
        return WorkResponse.builder()
                .id(work.getId())
                .name(work.getName())
                .laborPrice(work.getLaborPrice())
                .materialPrice(work.getMaterialPrice())
                .materialList(
                        work.getMaterialList().stream().map(workMaterial ->
                                BulkMaterialResponse.builder()
                                        .id(workMaterial.getMaterial().getId())
                                        .name(workMaterial.getMaterial().getName())
                                        .price(workMaterial.getPrice())
                                        .unitMeasure(new EnumDTO(
                                                workMaterial.getMaterial().getUnitMeasure().toString(),
                                                workMaterial.getMaterial().getUnitMeasure().getLabel()
                                        ))
                                        .quantityUnitMeasure(workMaterial.getBulkQuantity())
                                        .build()
                        ).toList()
                )
                .build();
    }
}
