package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.TourPackageRequest;
import com.smartrwanda.tourism.dto.TourPackageResponse;
import com.smartrwanda.tourism.entity.TourPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TourPackageMapper {

    @Mapping(source = "provider.id", target = "providerId")
    TourPackageResponse toResponse(TourPackage entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "available", ignore = true)
    TourPackage toEntity(TourPackageRequest request);
}