package com.iteletric.iteletricapi.dtos.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkRequestDTO {
    private String name;
    private BigDecimal price;
    private List<Long> materialIdList;
}
