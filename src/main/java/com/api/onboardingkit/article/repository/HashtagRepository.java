package com.api.onboardingkit.article.repository;

import com.api.onboardingkit.article.model.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query("SELECT h.content FROM Hashtag h WHERE h.articleId = :articleId")
    List<String> findHashtagsByArticleId(@Param("articleId") Long articleId);
}