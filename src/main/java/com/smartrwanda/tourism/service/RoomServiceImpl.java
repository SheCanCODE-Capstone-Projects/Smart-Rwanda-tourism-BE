package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.RoomRequest;
import com.smartrwanda.tourism.dto.RoomResponse;
import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.entity.Room;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.mapper.RoomMapper;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.repository.RoomRepository;
import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ProviderRepository providerRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomResponse create(Long providerId, RoomRequest request) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        if (provider.getCategory() != ProviderCategory.HOTEL
                && provider.getCategory() != ProviderCategory.MOTEL
                && provider.getCategory() != ProviderCategory.APARTMENT) {
            throw new BadRequestException("Only hotels, motels, and apartments can add rooms");
        }

        Room room = roomMapper.toEntity(request);
        room.setProvider(provider);

        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return roomMapper.toResponse(room);
    }

    @Override
    public List<RoomResponse> getByProvider(Long providerId) {
        return roomRepository.findByProviderId(providerId).stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    public RoomResponse update(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        room.setNumberOfBeds(request.getNumberOfBeds());
        room.setDescription(request.getDescription());
        room.setImageUrls(request.getImageUrls());
        room.setDiscount(request.getDiscount());

        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override
    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found");
        }
        roomRepository.deleteById(id);
    }
}
