package com.api.onboardingkit.article.repository;

import com.api.onboardingkit.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a ORDER BY a.postDate DESC")
    List<Article> findAllArticles();

    @Query("""
        SELECT a 
        FROM Article a 
        WHERE (:category IS NULL OR a.category = :category) 
          AND (:subcategory IS NULL OR a.subcategory = :subcategory) 
          AND (:title IS NULL OR a.title = :title) 
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
            @Param("title") String title,
            @Param("sortBy") String sortBy
    );

}