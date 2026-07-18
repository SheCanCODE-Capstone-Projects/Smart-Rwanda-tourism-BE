package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.ProviderRequest;
import com.smartrwanda.tourism.dto.ProviderResponse;
import com.smartrwanda.tourism.dto.ProviderSummaryResponse;
import com.smartrwanda.tourism.entity.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    @Mapping(source = "user.id", target = "userId")
    ProviderResponse toResponse(Provider entity);

    @Mapping(target = "coverImageUrl", expression = "java(entity.getImageUrls().isEmpty() ? null : entity.getImageUrls().get(0))")
    ProviderSummaryResponse toSummaryResponse(Provider entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    Provider toEntity(ProviderRequest request);
}

