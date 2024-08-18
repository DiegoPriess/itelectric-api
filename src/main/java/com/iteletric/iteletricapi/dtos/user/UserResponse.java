package com.iteletric.iteletricapi.dtos.user;

import com.iteletric.iteletricapi.models.Role;

import java.util.List;

public record UserResponse (

    Long id,

    String email,

    List<Role> roles
){}
