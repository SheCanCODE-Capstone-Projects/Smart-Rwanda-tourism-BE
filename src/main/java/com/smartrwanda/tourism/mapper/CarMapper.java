package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.CarRequest;
import com.smartrwanda.tourism.dto.CarResponse;
import com.smartrwanda.tourism.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "provider.id", target = "providerId")
    CarResponse toResponse(Car entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "available", ignore = true)
    Car toEntity(CarRequest request);
}
