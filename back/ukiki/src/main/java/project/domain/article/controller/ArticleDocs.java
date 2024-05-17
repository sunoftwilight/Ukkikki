package project.domain.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.request.ArticleUpdateDto;
import project.domain.article.dto.request.CommentDto;
import project.domain.article.dto.response.ArticlePageDto;
import project.domain.article.dto.response.SimpleArticleDto;
import project.global.result.ResultResponse;

import java.io.IOException;
import java.util.List;

@Tag(name = "ArticleController", description = "게시판 api")
public interface ArticleDocs {

    @Operation(summary = "게시판 생성")
    ResponseEntity<ResultResponse> create(Long partyId, ArticleCreateDto articleCreateDto, List<MultipartFile> multipartFiles);

    @Operation(summary = "게시판 디테일")
    ResponseEntity<ResultResponse> articleDetail(Long partyId, Long articleId);
    @Operation(summary = "게시판 리스트")
    ResponseEntity<ArticlePageDto> getArticleList(Long partyId, Pageable pageable);

    @Operation(summary = "게시판 수정")
    ResponseEntity<SimpleArticleDto> updateArticle(Long partyId, Long articleId, ArticleUpdateDto articleUpdateDto, List<MultipartFile> multipartFiles);
    @Operation(summary = "게시판 삭제")
    ResponseEntity<ResultResponse> deleteArticle(Long partyId, Long articleId);

    @Operation(summary = "포토 이동")
    void movePhoto(HttpServletResponse response, String fileId) throws IOException;

    @Operation(summary = "게시판 이동")
    void moveArticle(HttpServletResponse response, Long articleId) throws IOException;


    @Operation(summary = "댓글 조회")
    @GetMapping("/comment/{articleId}")
    ResponseEntity<ResultResponse> articleComment(@PathVariable(name = "articleId") Long articleId);

    @Operation(summary = "댓글 입력")
    @PostMapping("/comment/enter/{articleId}")
    ResponseEntity<ResultResponse> enterComment(@PathVariable(name = "articleId") Long articleId, @RequestBody CommentDto commentDto);

    @Operation(summary = "댓글 수정")
    @PatchMapping("/comment/modify/{articleId}/{commentIdx}")
    ResponseEntity<ResultResponse> modifyComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, @RequestBody CommentDto commentDto);

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comment/{articleId}/{commentIdx}")
    ResponseEntity<ResultResponse> deleteComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx);

    @Operation(summary = "대댓글 입력")
    @PostMapping("/reply/enter/{articleId}/{commentIdx}")
    ResponseEntity<ResultResponse> enterReply(@PathVariable(name = "articleId") Long articleId,@PathVariable(name = "commentIdx") Integer commentIdx, @RequestBody CommentDto commentDto);

    @Operation(summary = "대댓글 수정")
    @PatchMapping("/reply/modify/{articleId}/{commentIdx}/{replyIdx}")
    ResponseEntity<ResultResponse> modifyReply(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, @PathVariable(name = "replyIdx") Integer replyIdx, @RequestBody CommentDto commentDto);

    @Operation(summary = "대댓글 삭제")
    @DeleteMapping("/reply/{articleId}/{commentIdx}/{replyIdx}")
    ResponseEntity<ResultResponse> deleteReply(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentIdx") Integer commentIdx, @PathVariable(name = "replyIdx") Integer replyIdx);
}
