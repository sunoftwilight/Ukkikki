package project.domain.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.request.ArticleUpdateDto;
import project.domain.article.dto.response.ArticlePageDto;
import project.domain.article.dto.response.SimpleArticleDto;
import project.global.result.ResultResponse;

@Tag(name = "ArticleController", description = "게시판 api")
public interface ArticleDocs {

    @Operation(summary = "게시판 생성")
    ResponseEntity<ResultResponse> create(Long partyId, ArticleCreateDto articleCreateDto);

    @Operation(summary = "게시판 디테일")
    ResponseEntity<ResultResponse> articleDetail(Long partyId, Long articleId);
    @Operation(summary = "게시판 리스트")
    ResponseEntity<ArticlePageDto> getArticleList(Long partyId, Pageable pageable);

    @Operation(summary = "게시판 수정")
    ResponseEntity<SimpleArticleDto> updateArticle(Long partyId, Long articleId, ArticleUpdateDto articleUpdateDto);


    @Operation(summary = "댓글 조회")
    @GetMapping("/comment/{articleId}")
    ResponseEntity<ResultResponse> articleComment(@PathVariable(name = "articleId") Long articleId);

    @Operation(summary = "댓글 입력")
    @PostMapping("/comment/enter/{articleId}")
    ResponseEntity<ResultResponse> enterComment(@PathVariable(name = "articleId") Long articleId, String content);

    @Operation(summary = "댓글 수정")
    @PostMapping("/comment/modify/{articleId}/{commentIdx}")
    ResponseEntity<ResultResponse> modifyComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, String content);

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comment/{articleId}/{commentIdx}")
    ResponseEntity<ResultResponse> deleteComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx);

    @Operation(summary = "대댓글 입력")
    @PostMapping("/reply/enter/{articleId}/{commentIdx}")
    ResponseEntity<ResultResponse> enterReply(@PathVariable(name = "articleId") Long articleId,@PathVariable(name = "commentIdx") Integer commentIdx, String content);

    @Operation(summary = "대댓글 수정")
    @PostMapping("/reply/modify/{articleId}/{commentIdx}/{replyIdx}")
    ResponseEntity<ResultResponse> modifyReply(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, @PathVariable(name = "replyIdx") Integer replyIdx, String content);

    @Operation(summary = "대댓글 삭제")
    @DeleteMapping("/reply/{articleId}/{commentIdx}/{replyIdx}")
    ResponseEntity<ResultResponse> deleteReply(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, @PathVariable(name = "replyIdx") Integer replyIdx);
}
