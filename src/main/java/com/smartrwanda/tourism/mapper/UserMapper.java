package com.smartrwanda.tourism.mapper;


import com.smartrwanda.tourism.dto.UserResponse;
import com.smartrwanda.tourism.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "createdAt", target = "createdAt")
    UserResponse toResponse(User user);

    User toEntity(UserResponse userResponse);
}