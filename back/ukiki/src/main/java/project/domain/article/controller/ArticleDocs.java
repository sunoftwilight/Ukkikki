package project.domain.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import project.domain.article.dto.request.ArticleCreateDto;
import project.global.result.ResultResponse;

@Tag(name = "ArticleController", description = "게시판 api")
public interface ArticleDocs {

    @Operation(summary = "게시판 생성")
    ResponseEntity<ResultResponse> create(Long partyId, ArticleCreateDto articleCreateDto);
    
    @Operation(summary = "게시판 디테일")
    ResponseEntity<ResultResponse> articleDetail(Long partyId, Long articleId);


}
