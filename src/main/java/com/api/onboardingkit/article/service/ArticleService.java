package com.api.onboardingkit.article.service;

import com.api.onboardingkit.article.repository.ArticleRepository;
import com.api.onboardingkit.article.repository.HashtagRepository;
import com.api.onboardingkit.article.dto.ArticleRequestDTO;
import com.api.onboardingkit.article.dto.ArticleResponseDTO;
import com.api.onboardingkit.article.entity.Article;
import com.api.onboardingkit.article.entity.Hashtag;
import com.api.onboardingkit.article.dto.ArticleSearchDTO;
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

    public List<ArticleResponseDTO> fetchArticles(ArticleSearchDTO searchDTO) {
        List<Article> articles = articleRepository.findArticles(
                searchDTO.getCategory(),
                searchDTO.getSubcategory(),
                searchDTO.getTitle(),
                searchDTO.getSortBy()
        );

        return articles.stream()
                .map(article -> {
                    List<String> hashtags = hashtagRepository.findByArticleId(article.getId())
                            .stream()
                            .map(Hashtag::getContent)
                            .collect(Collectors.toList());
                    return ArticleResponseDTO.fromEntity(article, hashtags);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ArticleResponseDTO createArticle(
            ArticleRequestDTO requestDTO
    ) {
        Article article = Article.builder()
                .category(requestDTO.getCategory())
                .subcategory(requestDTO.getSubcategory())
                .postDate(requestDTO.getPostDate())
                .source(requestDTO.getSource())
                .title(requestDTO.getTitle())
                .summary(requestDTO.getSummary())
                .thumbnail(requestDTO.getThumbnail())
                .url(requestDTO.getUrl())
                .views(0)
                .createdTime(LocalDateTime.now()) // todo. requestDTO.getPostDate() 받는 부분 등록될때의 시간으로 변경
                .build();

        return ArticleResponseDTO.fromEntity(articleRepository.save(article), List.of());
    }

    @Transactional
    public String incrementViewsAndGetUrl(
            Long id
    ) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아티클이 존재하지 않습니다: " + id));

        article.incrementViews(); // todo. 별도 메서드 활용
        return article.getUrl();
    }

    @Transactional
    public void addHashtagToArticle(
            Long articleId,
            String hashtagContent
    ) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아티클이 존재하지 않습니다: " + articleId));

        Hashtag hashtag = Hashtag.builder()
                .articleId(article.getId())
                .content(hashtagContent)
                .createTime(LocalDateTime.now())
                .build();

        hashtagRepository.save(hashtag);
    }

}