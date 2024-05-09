package project.domain.article.service;

import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.response.ArticleCreateResDto;

public interface ArticleService {

    ArticleCreateResDto createArticle(Long partyId, ArticleCreateDto articleCreateDto);

}
