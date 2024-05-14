package project.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.article.collection.CommentCollection;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.dto.response.ArticlePageDto;
import project.domain.article.dto.response.SimpleArticleDto;
import project.domain.article.service.ArticleService;
import project.domain.article.service.CommentService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.util.List;

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
    public ResponseEntity<ResultResponse> modifyComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, String content) {

        commentService.modifyComment(articleId,commentIdx,content);

        return ResponseEntity.ok(new ResultResponse(ResultCode.MODIFY_MEMO_SUCCESS));
    }

    @Override
    @DeleteMapping("/comment/{articleId}/{commentIdx}")
    public ResponseEntity<ResultResponse> deleteComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx) {

        commentService.deleteComment(articleId, commentIdx);

        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_COMMENT_SUCCESS));
    }

    @Override
    @PostMapping("/reply/enter/{articleId}/{commentIdx}")
    public ResponseEntity<ResultResponse> enterReply(@PathVariable(name = "articleId") Long articleId,@PathVariable(name = "commentIdx") Integer commentIdx, String content) {
        commentService.enterReply(articleId,commentIdx,content);

        return ResponseEntity.ok(new ResultResponse(ResultCode.ENTER_COMMENT_SUCCESS));
    }

    @Override
    @PostMapping("/reply/modify/{articleId}/{commentIdx}/{replyIdx}")
    public ResponseEntity<ResultResponse> modifyReply(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, @PathVariable(name = "replyIdx") Integer replyIdx, String content) {
        commentService.modifyReply(articleId,commentIdx,replyIdx,content);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MODIFY_MEMO_SUCCESS));
    }

    @Override
    @DeleteMapping("/reply/{articleId}/{commentIdx}/{replyIdx}")
    public ResponseEntity<ResultResponse> deleteReply(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, @PathVariable(name = "replyIdx") Integer replyIdx) {
        commentService.deleteReply(articleId,commentIdx,replyIdx);
        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_COMMENT_SUCCESS));
    }

    @Override
    public ResponseEntity<ArticlePageDto> getArticleList(Long partyId, @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        ArticlePageDto res = articleService.getArticleList(partyId, pageable);
        return ResponseEntity.ok(res);
    }




}
