package com.api.onboardingkit.article.service;

import com.api.onboardingkit.article.repository.ArticleRepository;
import com.api.onboardingkit.article.repository.HashtagRepository;
import com.api.onboardingkit.article.dto.ArticleRequestDTO;
import com.api.onboardingkit.article.dto.ArticleResponseDTO;
import com.api.onboardingkit.article.entity.Article;
import com.api.onboardingkit.article.entity.Hashtag;
import com.api.onboardingkit.article.dto.ArticleSearchDTO;
import com.api.onboardingkit.article.repository.specification.ArticleSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        Specification<Article> spec = Specification
                .where(ArticleSpecification.categoryEquals(searchDTO.getCategory()))
                .and(ArticleSpecification.subcategoryEquals(searchDTO.getSubcategory()))
                .and(ArticleSpecification.titleContains(searchDTO.getTitle()));

        Sort sort = ArticleSpecification.getSort(searchDTO.getSortBy());

        List<Article> articles = articleRepository.findAll(spec, sort);

        return articles.stream()
                .map(article -> {
                    List<String> hashtags = hashtagRepository.findByArticleId(article.getId())
                            .stream().map(Hashtag::getContent)
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
                .createdTime(LocalDateTime.now())
                .build();

        return ArticleResponseDTO.fromEntity(articleRepository.save(article), List.of());
    }

    @Transactional
    public String incrementViewsAndGetUrl(
            Long id
    ) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아티클이 존재하지 않습니다: " + id));

        article.incrementViews();
        return article.getUrl();
    }

    @Transactional
    public void addHashtagToArticle(
            Long articleId,
            String hashtagContent
    ) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아티클이 존재하지 않습니다: " + articleId));

        // 해시태그 개수 세기
        List<Hashtag> existingHashtags = hashtagRepository.findByArticleId(articleId);
        int hashtagCount = existingHashtags.size();

        // 해시태그 개수 3개로 제한
        if (hashtagCount >= 3) {
            throw new IllegalArgumentException("해시태그는 최대 3개까지만 추가할 수 있습니다.");
        }

        // 해시태그 개수(등록할 해시태그 1개 포함)에 따른 최대 길이 설정
        int maxLength = switch (hashtagCount + 1) {
            case 2 -> 18;
            case 3 -> 15;
            case 1 -> 20;
            default -> 20;
        };

        // 공백 포함 글자수 최대 길이에 맞춤
        if (hashtagContent.length() > maxLength) {
            hashtagContent = hashtagContent.substring(0, maxLength);
        }

        Hashtag hashtag = Hashtag.builder()
                .articleId(article.getId())
                .content(hashtagContent)
                .createTime(LocalDateTime.now())
                .build();

        hashtagRepository.save(hashtag);
    }

}