package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.DiscoverItem;
import com.smartrwanda.tourism.entity.MainCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DiscoverRepository extends JpaRepository<DiscoverItem, Long> {



    List<DiscoverItem> findByMainCategory(MainCategory mainCategory);

    Page<DiscoverItem> findByMainCategory(MainCategory mainCategory, Pageable pageable);

    List<DiscoverItem> findByMainCategoryAndIsActiveTrue(MainCategory mainCategory);



    List<DiscoverItem> findBySubCategory(String subCategory);

    List<DiscoverItem> findByMainCategoryAndSubCategory(MainCategory mainCategory, String subCategory);

    Page<DiscoverItem> findByMainCategoryAndSubCategory(MainCategory mainCategory, String subCategory, Pageable pageable);



    List<DiscoverItem> findByLocationContainingIgnoreCase(String location);



    List<DiscoverItem> findByIsActiveTrue();

    List<DiscoverItem> findByFeaturedTrue();

    List<DiscoverItem> findByMainCategoryAndFeaturedTrue(MainCategory mainCategory);



    @Query("SELECT d FROM DiscoverItem d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<DiscoverItem> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT d FROM DiscoverItem d WHERE d.mainCategory = :mainCategory AND " +
            "(LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<DiscoverItem> searchByCategoryAndKeyword(@Param("mainCategory") MainCategory mainCategory,
                                                  @Param("keyword") String keyword);



    @Query("SELECT d FROM DiscoverItem d WHERE d.mainCategory = :mainCategory AND d.isActive = true ORDER BY d.averageRating DESC")
    List<DiscoverItem> findTopRatedByMainCategory(@Param("mainCategory") MainCategory mainCategory, Pageable pageable);

    List<DiscoverItem> findTop10ByOrderByAverageRatingDesc();



    long countByMainCategory(MainCategory mainCategory);

    long countByMainCategoryAndIsActiveTrue(MainCategory mainCategory);

    @Query("SELECT d.subCategory, COUNT(d) FROM DiscoverItem d WHERE d.mainCategory = :mainCategory GROUP BY d.subCategory")
    List<Object[]> countBySubCategory(@Param("mainCategory") MainCategory mainCategory);



    @Query("SELECT d FROM DiscoverItem d WHERE d.mainCategory = :mainCategory AND d.price BETWEEN :minPrice AND :maxPrice")
    List<DiscoverItem> findByPriceRange(@Param("mainCategory") MainCategory mainCategory,
                                        @Param("minPrice") BigDecimal minPrice,
                                        @Param("maxPrice") BigDecimal maxPrice);



    List<DiscoverItem> findTop10ByOrderByCreatedAtDesc();

    List<DiscoverItem> findByMainCategoryOrderByCreatedAtDesc(MainCategory mainCategory, Pageable pageable);
}