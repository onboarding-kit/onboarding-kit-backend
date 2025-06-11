package com.api.onboardingkit.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    @Schema(description = "카테고리 이름", example = "개발")
    private String categoryName;

    @Schema(description = "카테고리(개발/기획/다자인)는 0, 서브카테고리는 1", example = "0")
    private Integer depth;

    @Schema(description = "부모 카테고리 ID (depth가 1 이상인 경우)", example = "1")
    private Long parentId;
}
