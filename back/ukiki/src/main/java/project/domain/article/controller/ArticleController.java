package project.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.service.ArticleService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController implements ArticleDocs{


    private final ArticleService articleService;

    @Override
    @PostMapping("/create/{partyId}")
    public ResponseEntity<ResultResponse> create(@PathVariable(name = "partyId") Long partyId, @RequestBody ArticleCreateDto articleCreateDto) {
        ArticleCreateResDto res = articleService.createArticle(partyId, articleCreateDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_ARTICLE_SUCCESS, res));
    }

    @Override
    public ResponseEntity<ResultResponse> articleDetail(Long partyId, Long articleId) {
        return null;
    }


}
