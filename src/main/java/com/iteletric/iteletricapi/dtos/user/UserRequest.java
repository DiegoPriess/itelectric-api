package com.iteletric.iteletricapi.dtos.user;

import com.iteletric.iteletricapi.enums.user.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest (
    @NotBlank
    String name,

    @NotBlank
    @Email
    String email,

    @NotBlank
    String password,

    @NotNull
    RoleName role
){}