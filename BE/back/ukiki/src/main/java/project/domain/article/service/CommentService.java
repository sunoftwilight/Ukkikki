package project.domain.article.service;

import project.domain.article.collection.CommentCollection;
import project.domain.article.dto.request.CommentDto;
import project.domain.article.dto.response.ArticleCreateResDto;

public interface CommentService {
    // 댓글 관리
    void createComment(ArticleCreateResDto articleCreateResDto);
    CommentCollection articleComment(Long articleId);
    void enterComment(Long articleId, CommentDto commentDto);
    void modifyComment(Long articleId, Integer commentIdx, CommentDto commentDto);
    void deleteComment(Long articleId, Integer commentIdx);
    void enterReply(Long articleId, Integer commentIdx, CommentDto commentDto);
    void modifyReply(Long articleId, Integer commentIdx, Integer replyIdx, CommentDto commentDto);
    void deleteReply(Long articleId, Integer commentIdx, Integer replyIdx);
}
