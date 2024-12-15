package com.iteletric.iteletricapi.dtos.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkMaterialRequest {

    private Long id;

    private Integer bulkQuantity;
}
