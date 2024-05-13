package project.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.dto.response.SimpleArticleDto;
import project.domain.article.service.ArticleService;
import project.domain.article.service.CommentService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController implements ArticleDocs{


    private final ArticleService articleService;
    private final CommentService createComment;


    @Override
    @PostMapping("/create/{partyId}")
    public ResponseEntity<ResultResponse> create(@PathVariable(name = "partyId") Long partyId, @RequestBody ArticleCreateDto articleCreateDto) {
        ArticleCreateResDto res = articleService.createArticle(partyId, articleCreateDto);

        // 댓글 mongoDB create
        createComment.createComment(res);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_ARTICLE_SUCCESS, res));
    }

    @Override
    @GetMapping("/detail/{partyId}/{articleId}")
    public ResponseEntity<ResultResponse> articleDetail(@PathVariable(name = "partyId") Long partyId, @PathVariable(name = "articleId") Long articleId) {
        SimpleArticleDto res = articleService.getArticleDetail(partyId, articleId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_ARTICLE_SUCCESS, res));
    }


}
