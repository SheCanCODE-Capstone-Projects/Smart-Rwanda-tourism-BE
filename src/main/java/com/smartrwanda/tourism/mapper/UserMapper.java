package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.UserResponse;
import com.smartrwanda.tourism.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User entity);
}