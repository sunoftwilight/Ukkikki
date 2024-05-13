package project.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.article.collection.CommentCollection;
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
    private final CommentService commentService;


    @Override
    @PostMapping("/create/{partyId}")
    public ResponseEntity<ResultResponse> create(@PathVariable(name = "partyId") Long partyId, @RequestBody ArticleCreateDto articleCreateDto) {
        ArticleCreateResDto res = articleService.createArticle(partyId, articleCreateDto);

        // 댓글 mongoDB create
        commentService.createComment(res);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_ARTICLE_SUCCESS, res));
    }

    @Override
    @GetMapping("/detail/{partyId}/{articleId}")
    public ResponseEntity<ResultResponse> articleDetail(@PathVariable(name = "partyId") Long partyId, @PathVariable(name = "articleId") Long articleId) {
        SimpleArticleDto res = articleService.getArticleDetail(partyId, articleId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_ARTICLE_SUCCESS, res));
    }

    @Override
    @GetMapping("/comment/{articleId}")
    public ResponseEntity<ResultResponse> articleComment(@PathVariable(name = "articleId") Long articleId) {

        CommentCollection res = commentService.articleComment(articleId);

        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_COMMENT_SUCCESS, res));
    }

    @Override
    @PostMapping("/comment/enter/{articleId}")
    public ResponseEntity<ResultResponse> enterComment(@PathVariable(name = "articleId")Long articleId, String content) {

        commentService.enterComment(articleId, content);

        return ResponseEntity.ok(new ResultResponse(ResultCode.ENTER_COMMENT_SUCCESS));
    }

    @Override
    @PostMapping("/comment/modify/{articleId}/{commentIdx}")
    public ResponseEntity<ResultResponse> modifyComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Long commentIdx, String content) {

        commentService.modifyComment(articleId,commentIdx,content);

        return ResponseEntity.ok(new ResultResponse(ResultCode.MODIFY_MEMO_SUCCESS));
    }

    @Override
    @DeleteMapping("/comment/{articleId}/{commentIdx}")
    public ResponseEntity<ResultResponse> deleteComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Long commentIdx) {

        commentService.deleteComment(articleId, commentIdx);

        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_COMMENT_SUCCESS));
    }


}
