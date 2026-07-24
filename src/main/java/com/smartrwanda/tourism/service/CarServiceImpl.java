package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.CarRequest;
import com.smartrwanda.tourism.dto.CarResponse;
import com.smartrwanda.tourism.entity.Car;
import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.mapper.CarMapper;
import com.smartrwanda.tourism.repository.CarRepository;
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
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ProviderRepository providerRepository;
    private final CarMapper carMapper;

    @Override
    public CarResponse create(Long providerId, CarRequest request) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        if (provider.getCategory() != ProviderCategory.CAR_RENTAL) {
            throw new BadRequestException("Only car rentals can add cars");
        }

        Car car = carMapper.toEntity(request);
        car.setProvider(provider);

        return carMapper.toResponse(carRepository.save(car));
    }

    @Override
    public CarResponse getById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        return carMapper.toResponse(car);
    }

    @Override
    public List<CarResponse> getByProvider(Long providerId) {
        return carRepository.findByProviderId(providerId).stream()
                .map(carMapper::toResponse)
                .toList();
    }

    @Override
    public CarResponse update(Long id, CarRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        car.setCarName(request.getCarName());
        car.setPricePerDay(request.getPricePerDay());
        car.setNumberOfSeats(request.getNumberOfSeats());
        car.setTransmission(request.getTransmission());
        car.setDescription(request.getDescription());
        car.setImageUrls(request.getImageUrls());
        car.setDiscount(request.getDiscount());

        return carMapper.toResponse(carRepository.save(car));
    }

    @Override
    public void delete(Long id) {
        if (!carRepository.existsById(id)) {
            throw new ResourceNotFoundException("Car not found");
        }
        carRepository.deleteById(id);
    }
}
