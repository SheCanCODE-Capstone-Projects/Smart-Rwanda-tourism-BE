package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.UserResponse;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User entity);
}
