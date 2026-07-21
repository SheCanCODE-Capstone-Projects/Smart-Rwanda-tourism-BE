package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.MainCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscoverFilterRequest {

    private MainCategory mainCategory;

    private String subCategory;

    private String location;

    private String keyword;

    private Boolean featured;

    private Double minRating;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private String sortBy = "createdAt";

    private String sortDirection = "DESC";

    private int page = 0;

    private int size = 10;
}