package com.iteletric.iteletricapi.mappers;

import com.iteletric.iteletricapi.dtos.user.UserResponse;
import com.iteletric.iteletricapi.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponseDTO(User user);
}
