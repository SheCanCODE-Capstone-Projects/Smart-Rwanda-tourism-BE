package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.DishRequest;
import com.smartrwanda.tourism.dto.DishResponse;
import com.smartrwanda.tourism.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(source = "provider.id", target = "providerId")
    DishResponse toResponse(Dish entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "available", ignore = true)
    Dish toEntity(DishRequest request);
}
