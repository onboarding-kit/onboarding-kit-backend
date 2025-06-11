package com.api.onboardingkit.article.repository;

import com.api.onboardingkit.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("""
        SELECT a FROM Article a 
        WHERE a.categoryId = :categoryId
          AND a.subcategoryId = :subcategoryId
          AND (:title IS NULL OR a.title LIKE %:title%)
        ORDER BY 
          CASE WHEN :sortBy = 'latest' THEN a.postDate ELSE NULL END DESC, 
          CASE WHEN :sortBy = 'popular' THEN a.views ELSE 0 END DESC
    """)
    List<Article> findArticles(
            @Param("categoryId") Long categoryId,
            @Param("subcategoryId") Long subcategoryId,
            @Param("title") String title,
            @Param("sortBy") String sortBy
    );

    List<Article> findTop3ByOrderByViewsDesc();
    
}