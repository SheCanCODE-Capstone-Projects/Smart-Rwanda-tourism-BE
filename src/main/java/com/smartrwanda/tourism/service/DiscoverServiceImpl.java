package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.DiscoverFilterRequest;
import com.smartrwanda.tourism.dto.DiscoverItemRequest;
import com.smartrwanda.tourism.dto.DiscoverCategoryResponse;
import com.smartrwanda.tourism.dto.DiscoverItemResponse;
import com.smartrwanda.tourism.entity.DiscoverItem;
import com.smartrwanda.tourism.entity.MainCategory;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.DiscoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscoverServiceImpl implements DiscoverService {

    private final DiscoverRepository discoverRepository;

    @Override
    @Transactional
    public DiscoverItemResponse createDiscoverItem(DiscoverItemRequest request) {
        DiscoverItem item = new DiscoverItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setMainCategory(request.getMainCategory());
        item.setSubCategory(request.getSubCategory());
        item.setLocation(request.getLocation());
        item.setLatitude(request.getLatitude());
        item.setLongitude(request.getLongitude());
        item.setImages(request.getImages());
        item.setOpeningHours(request.getOpeningHours());
        item.setPrice(request.getPrice());
        item.setContactInfo(request.getContactInfo());
        item.setWebsite(request.getWebsite());
        item.setFeatured(request.getFeatured() != null && request.getFeatured());
        item.setStarRating(request.getStarRating());
        item.setVehicleTypes(request.getVehicleTypes());
        item.setCuisineType(request.getCuisineType());
        item.setEntranceFee(request.getEntranceFee());

        DiscoverItem saved = discoverRepository.save(item);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public DiscoverItemResponse updateDiscoverItem(Long id, DiscoverItemRequest request) {
        DiscoverItem existing = discoverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discover item not found with id: " + id));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setMainCategory(request.getMainCategory());
        existing.setSubCategory(request.getSubCategory());
        existing.setLocation(request.getLocation());
        existing.setLatitude(request.getLatitude());
        existing.setLongitude(request.getLongitude());
        existing.setImages(request.getImages());
        existing.setOpeningHours(request.getOpeningHours());
        existing.setPrice(request.getPrice());
        existing.setContactInfo(request.getContactInfo());
        existing.setWebsite(request.getWebsite());
        existing.setFeatured(request.getFeatured() != null && request.getFeatured());
        existing.setStarRating(request.getStarRating());
        existing.setVehicleTypes(request.getVehicleTypes());
        existing.setCuisineType(request.getCuisineType());
        existing.setEntranceFee(request.getEntranceFee());

        DiscoverItem updated = discoverRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public DiscoverItemResponse getDiscoverItemById(Long id) {
        DiscoverItem item = discoverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discover item not found with id: " + id));
        return mapToResponse(item);
    }

    @Override
    public List<DiscoverItemResponse> getAllDiscoverItems() {
        return discoverRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DiscoverItemResponse> getAllDiscoverItemsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return discoverRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void deleteDiscoverItem(Long id) {
        if (!discoverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Discover item not found with id: " + id);
        }
        discoverRepository.deleteById(id);
    }

    @Override
    public List<DiscoverItemResponse> getByMainCategory(MainCategory mainCategory) {
        return discoverRepository.findByMainCategory(mainCategory).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DiscoverItemResponse> getByMainCategoryPaginated(MainCategory mainCategory, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return discoverRepository.findByMainCategory(mainCategory, pageable).map(this::mapToResponse);
    }

    @Override
    public List<DiscoverItemResponse> getByMainCategoryAndSubCategory(MainCategory mainCategory, String subCategory) {
        return discoverRepository.findByMainCategoryAndSubCategory(mainCategory, subCategory).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DiscoverItemResponse> getByMainCategoryAndSubCategoryPaginated(MainCategory mainCategory,
                                                                               String subCategory,
                                                                               int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return discoverRepository.findByMainCategoryAndSubCategory(mainCategory, subCategory, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public List<DiscoverItemResponse> searchDiscoverItems(String keyword) {
        return discoverRepository.searchByKeyword(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscoverItemResponse> searchByCategoryAndKeyword(MainCategory mainCategory, String keyword) {
        return discoverRepository.searchByCategoryAndKeyword(mainCategory, keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscoverItemResponse> getFeaturedItems() {
        return discoverRepository.findByFeaturedTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscoverItemResponse> getFeaturedByCategory(MainCategory mainCategory) {
        return discoverRepository.findByMainCategoryAndFeaturedTrue(mainCategory).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscoverItemResponse> getRecentItems() {
        return discoverRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscoverItemResponse> getRecentByCategory(MainCategory mainCategory) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        return discoverRepository.findByMainCategoryOrderByCreatedAtDesc(mainCategory, pageable).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscoverItemResponse> getTopRatedByCategory(MainCategory mainCategory) {
        Pageable pageable = PageRequest.of(0, 10);
        return discoverRepository.findTopRatedByMainCategory(mainCategory, pageable).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscoverItemResponse> getTopRatedAll() {
        return discoverRepository.findTop10ByOrderByAverageRatingDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DiscoverItemResponse> filterDiscoverItems(DiscoverFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(),
                Sort.by(Sort.Direction.fromString(filter.getSortDirection()), filter.getSortBy()));
        return discoverRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public List<DiscoverCategoryResponse> getAllCategoriesWithCounts() {
        List<DiscoverCategoryResponse> categories = new ArrayList<>();
        for (MainCategory mainCategory : MainCategory.values()) {
            categories.add(getCategoryDetails(mainCategory));
        }
        return categories;
    }

    @Override
    public DiscoverCategoryResponse getCategoryDetails(MainCategory mainCategory) {
        List<Object[]> subCategoryCounts = discoverRepository.countBySubCategory(mainCategory);
        long totalItems = discoverRepository.countByMainCategory(mainCategory);

        List<DiscoverCategoryResponse.SubCategoryInfo> subCategories = new ArrayList<>();
        for (Object[] result : subCategoryCounts) {
            String subCategory = (String) result[0];
            Long count = (Long) result[1];
            DiscoverCategoryResponse.SubCategoryInfo info = DiscoverCategoryResponse.SubCategoryInfo.builder()
                    .code(subCategory)
                    .displayName(subCategory.replace("_", " "))
                    .count(count)
                    .build();
            subCategories.add(info);
        }

        return DiscoverCategoryResponse.builder()
                .mainCategory(mainCategory.name())
                .displayName(mainCategory.name())
                .subCategories(subCategories)
                .totalItems(totalItems)
                .build();
    }

    @Override
    public List<DiscoverItemResponse> getByLocation(String location) {
        return discoverRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    private DiscoverItemResponse mapToResponse(DiscoverItem item) {
        return DiscoverItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .mainCategory(item.getMainCategory())
                .subCategory(item.getSubCategory())
                .location(item.getLocation())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .images(item.getImages())
                .openingHours(item.getOpeningHours())
                .price(item.getPrice())
                .contactInfo(item.getContactInfo())
                .website(item.getWebsite())
                .averageRating(item.getAverageRating())
                .totalReviews(item.getTotalReviews())
                .isActive(item.getIsActive())
                .featured(item.getFeatured())
                .starRating(item.getStarRating())
                .vehicleTypes(item.getVehicleTypes())
                .cuisineType(item.getCuisineType())
                .entranceFee(item.getEntranceFee())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}