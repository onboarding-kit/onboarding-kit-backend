package com.example.onboardingkitbackend.article.repository;

import com.example.onboardingkitbackend.article.model.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}