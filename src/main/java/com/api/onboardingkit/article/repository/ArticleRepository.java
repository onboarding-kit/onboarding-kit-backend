package com.api.onboardingkit.article.repository;

import com.api.onboardingkit.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    // 조건에 맞는 아티클 반환
    @Query("""
        SELECT a 
        FROM Article a 
        WHERE (:category IS NULL OR a.category = :category) 
          AND (:subcategory IS NULL OR a.subcategory = :subcategory) 
        ORDER BY 
          CASE 
            WHEN :sortBy = 'latest' THEN a.postDate 
            ELSE NULL
          END DESC, 
          CASE 
            WHEN :sortBy = 'popular' THEN a.views 
            ELSE 0 
          END DESC
    """)
    List<Article> findArticles(
            @Param("category") String category,
            @Param("subcategory") String subcategory,
            @Param("sortBy") String sortBy
    );

    // 아티클 검색
    @Query("""
    SELECT DISTINCT a, 
           CASE WHEN :sortBy = 'latest' THEN a.postDate ELSE NULL END AS sortPostDate,
           CASE WHEN :sortBy = 'popular' THEN a.views ELSE 0 END AS sortViews
    FROM Article a
    LEFT JOIN a.hashtags h
    WHERE (:category IS NULL OR a.category = :category)
      AND (:subcategory IS NULL OR a.subcategory = :subcategory)
      AND (:searchTerm IS NULL OR a.title LIKE %:searchTerm% OR h.content LIKE %:searchTerm%)
    ORDER BY sortPostDate DESC, sortViews DESC
    """)
    List<Article> searchArticles(
            @Param("category") String category,
            @Param("subcategory") String subcategory,
            @Param("searchTerm") String searchTerm,
            @Param("sortBy") String sortBy
    );

    // 모든 아티클 반환
    @Query("SELECT a FROM Article a ORDER BY a.postDate DESC")
    List<Article> findAllArticles();
}