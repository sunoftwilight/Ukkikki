package project.domain.article.service;

import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.dto.response.SimpleArticleDto;

public interface ArticleService {

    ArticleCreateResDto createArticle(Long partyId, ArticleCreateDto articleCreateDto);
    SimpleArticleDto getArticleDetail(Long partyId, Long articleId);

}
