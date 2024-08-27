package com.iteletric.iteletricapi.dtos.user;

import com.iteletric.iteletricapi.models.Role;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse (

    Long id,

    String email,

    String name,

    Integer deleted,

    LocalDateTime dateCreated,

    LocalDateTime dateUpdated,

    List<Role> roles
){}
