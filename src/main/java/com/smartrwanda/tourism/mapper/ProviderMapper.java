package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.ProviderRequest;
import com.smartrwanda.tourism.dto.ProviderResponse;
import com.smartrwanda.tourism.dto.ProviderSummaryResponse;
import com.smartrwanda.tourism.entity.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProviderMapper {

    ProviderResponse toResponse(Provider provider);

    @Mapping(target = "contactPhone", source = "phone")
    @Mapping(target = "contactEmail", source = "email")
    Provider toEntity(ProviderRequest request);

    ProviderSummaryResponse toSummaryResponse(Provider provider);
}