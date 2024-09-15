package com.iteletric.iteletricapi.dtos.user;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        LocalDateTime dateCreated,
        LocalDateTime dateUpdated
) {
}
