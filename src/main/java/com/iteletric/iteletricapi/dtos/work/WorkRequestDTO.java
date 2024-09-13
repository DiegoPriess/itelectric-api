package com.iteletric.iteletricapi.dtos.work;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WorkRequestDTO {
    private String name;
    private BigDecimal price;
    private List<Long> materialIdList;
}
