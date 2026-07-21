package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.DishRequest;
import com.smartrwanda.tourism.dto.DishResponse;
import com.smartrwanda.tourism.entity.Dish;
import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.mapper.DishMapper;
import com.smartrwanda.tourism.repository.DishRepository;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final ProviderRepository providerRepository;
    private final DishMapper dishMapper;

    @Override
    public DishResponse create(Long providerId, DishRequest request) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        if (provider.getCategory() != ProviderCategory.RESTAURANT) {
            throw new BadRequestException("Only restaurants can add dishes");
        }

        Dish dish = dishMapper.toEntity(request);
        dish.setProvider(provider);

        return dishMapper.toResponse(dishRepository.save(dish));
    }

    @Override
    public DishResponse getById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return dishMapper.toResponse(dish);
    }

    @Override
    public List<DishResponse> getByProvider(Long providerId) {
        return dishRepository.findByProviderId(providerId).stream()
                .map(dishMapper::toResponse)
                .toList();
    }

    @Override
    public DishResponse update(Long id, DishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));

        dish.setName(request.getName());
        dish.setPrice(request.getPrice());
        dish.setDescription(request.getDescription());
        dish.setSpicy(request.isSpicy());
        dish.setImageUrls(request.getImageUrls());
        dish.setDiscount(request.getDiscount());

        return dishMapper.toResponse(dishRepository.save(dish));
    }

    @Override
    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found");
        }
        dishRepository.deleteById(id);
    }
}
