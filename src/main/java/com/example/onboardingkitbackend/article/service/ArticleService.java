package com.example.onboardingkitbackend.article.service;

import com.example.onboardingkitbackend.article.model.Article;
import com.example.onboardingkitbackend.article.model.Hashtag;
import com.example.onboardingkitbackend.article.repository.ArticleRepository;
import com.example.onboardingkitbackend.article.repository.HashtagRepository;
import com.example.onboardingkitbackend.article.response.ArticleResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final HashtagRepository hashtagRepository;


    public List<ArticleResponseDTO> fetchArticles(String category, String subcategory, String sortBy) {
        List<Article> articles;

        if (category == null && subcategory == null) {
            // 조건이 없으면 모든 아티클 조회
            articles = articleRepository.findAllArticles();
        } else {
            // 조건이 있으면 조건에 맞는 아티클 조회
            articles = articleRepository.findArticles(category, subcategory, sortBy);
        }

        return articles.stream()
                .map(ArticleResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDTO> searchArticles(String category, String subcategory, String searchTerm, String sortBy) {
        List<Article> articles = articleRepository.searchArticles(category, subcategory, searchTerm, sortBy);
        return articles.stream()
                .map(ArticleResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public String incrementViewsAndGetUrl(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아티클이 존재하지 않습니다: " + id));
        article.setViews(article.getViews() + 1);
        articleRepository.save(article);
        return article.getUrl();
    }

    public void addHashtagsToArticle(Long articleId, List<String> hashtags) {
        // Article 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아티클이 존재하지 않습니다: " + articleId));

        // 해시태그 저장
        for (String tag : hashtags) {
            Hashtag hashtag = new Hashtag();
            hashtag.setArticle(article); // Article과 연결
            hashtag.setContent(tag); // 해시태그 내용 설정
            hashtag.setCreateTime(LocalDateTime.now()); // 생성 시간 설정
            hashtagRepository.save(hashtag); // 저장
        }
    }
}
