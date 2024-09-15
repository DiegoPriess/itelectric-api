package com.iteletric.iteletricapi.dtos.work;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.config.validation.customvalidation.optionalnotblanck.OptionalNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkRequestDTO {

    @NotBlank(groups = ValidationGroups.POST.class, message = "O nome deve ser informado!")
    @OptionalNotBlank(groups = ValidationGroups.PUT.class, message = "O nome não pode ser vazio!")
    private String name;

    private BigDecimal price;

    private List<Long> materialIdList;
}
