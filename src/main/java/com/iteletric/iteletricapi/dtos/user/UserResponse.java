package com.iteletric.iteletricapi.dtos.user;

import com.iteletric.iteletricapi.models.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;

    private String name;

    private String email;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;

    public static UserResponse convert(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .dateCreated(user.getDateCreated())
                .dateUpdated(user.getDateUpdated())
                .build();
    }

    public static Page<UserResponse> convert(Page<User> userPage) {
        return userPage.map(user ->
                UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .dateCreated(user.getDateCreated())
                        .dateUpdated(user.getDateUpdated())
                        .build()
        );
    }
}
