package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.DiscoverFilterRequest;
import com.smartrwanda.tourism.dto.DiscoverItemRequest;
import com.smartrwanda.tourism.dto.DiscoverCategoryResponse;
import com.smartrwanda.tourism.dto.DiscoverItemResponse;
import com.smartrwanda.tourism.entity.MainCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscoverService {


    DiscoverItemResponse createDiscoverItem(DiscoverItemRequest request);

    DiscoverItemResponse updateDiscoverItem(Long id, DiscoverItemRequest request);

    DiscoverItemResponse getDiscoverItemById(Long id);

    List<DiscoverItemResponse> getAllDiscoverItems();

    Page<DiscoverItemResponse> getAllDiscoverItemsPaginated(int page, int size);

    void deleteDiscoverItem(Long id);


    List<DiscoverItemResponse> getByMainCategory(MainCategory mainCategory);

    Page<DiscoverItemResponse> getByMainCategoryPaginated(MainCategory mainCategory, int page, int size);

    List<DiscoverItemResponse> getByMainCategoryAndSubCategory(MainCategory mainCategory, String subCategory);

    Page<DiscoverItemResponse> getByMainCategoryAndSubCategoryPaginated(MainCategory mainCategory,
                                                                        String subCategory,
                                                                        int page, int size);


    List<DiscoverItemResponse> searchDiscoverItems(String keyword);

    List<DiscoverItemResponse> searchByCategoryAndKeyword(MainCategory mainCategory, String keyword);


    List<DiscoverItemResponse> getFeaturedItems();

    List<DiscoverItemResponse> getFeaturedByCategory(MainCategory mainCategory);

    List<DiscoverItemResponse> getRecentItems();

    List<DiscoverItemResponse> getRecentByCategory(MainCategory mainCategory);


    List<DiscoverItemResponse> getTopRatedByCategory(MainCategory mainCategory);

    List<DiscoverItemResponse> getTopRatedAll();


    Page<DiscoverItemResponse> filterDiscoverItems(DiscoverFilterRequest filter);


    List<DiscoverCategoryResponse> getAllCategoriesWithCounts();

    DiscoverCategoryResponse getCategoryDetails(MainCategory mainCategory);

    List<DiscoverItemResponse> getByLocation(String location);
}