package com.smartrwanda.tourism.mapper;

import com.smartrwanda.tourism.dto.RoomRequest;
import com.smartrwanda.tourism.dto.RoomResponse;
import com.smartrwanda.tourism.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "provider.id", target = "providerId")
    RoomResponse toResponse(Room entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "available", ignore = true)
    Room toEntity(RoomRequest request);
}
