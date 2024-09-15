package com.iteletric.iteletricapi.dtos.user;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.config.validation.customvalidation.optionalnotblanck.OptionalNotBlank;
import com.iteletric.iteletricapi.enums.user.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(groups = ValidationGroups.POST.class, message = "O nome deve ser informado!")
    @OptionalNotBlank(groups = ValidationGroups.PUT.class, message = "O nome não pode ser vazio!")
    private String name;


    @NotBlank(groups = ValidationGroups.POST.class, message = "O email deve ser informado!")
    @OptionalNotBlank(groups = ValidationGroups.PUT.class, message = "O email não pode ser vazio!")
    @Email(message = "O email informado é inválido!")
    private String email;

    @NotBlank(groups = ValidationGroups.POST.class, message = "A senha deve ser informada!")
    @OptionalNotBlank(groups = ValidationGroups.PUT.class, message = "A senha não pode ser vazia!")
    private String password;

    @NotNull(groups = ValidationGroups.POST.class, message = "As permissões do usuário devem ser informadas!")
    private RoleName role;
}
