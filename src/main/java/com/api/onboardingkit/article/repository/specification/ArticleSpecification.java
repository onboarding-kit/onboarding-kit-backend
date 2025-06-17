package com.api.onboardingkit.article.repository.specification;

import com.api.onboardingkit.article.entity.Article;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;

public class ArticleSpecification {

    public static Specification<Article> categoryIdEquals(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("categoryId"), categoryId);
    }

    public static Specification<Article> subcategoryIdEquals(Long subcategoryId) {
        return (root, query, cb) ->
                subcategoryId == null ? null : cb.equal(root.get("subcategoryId"), subcategoryId);
    }

    public static Specification<Article> titleContains(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(root.get("title"), "%" + title + "%");
    }

    public static Sort getSort(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "postDate");
        }

        return switch (sortBy) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "postDate");
            case "popular" -> Sort.by(Sort.Direction.DESC, "views");
            default -> Sort.by(Sort.Direction.DESC, "postDate");
        };
    }

}
