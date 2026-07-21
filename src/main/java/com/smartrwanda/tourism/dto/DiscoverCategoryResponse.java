package com.smartrwanda.tourism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscoverCategoryResponse {

    private String mainCategory;

    private String displayName;

    private List<SubCategoryInfo> subCategories;

    private Long totalItems;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategoryInfo {
        private String code;
        private String displayName;
        private Long count;
    }
}