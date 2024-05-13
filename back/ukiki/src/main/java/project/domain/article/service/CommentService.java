package project.domain.article.service;

import project.domain.article.collection.CommentCollection;
import project.domain.article.dto.response.ArticleCreateResDto;

public interface CommentService {
    // 댓글 관리
    void createComment(ArticleCreateResDto articleCreateResDto);
    CommentCollection articleComment(Long articleId);
    void enterComment(Long articleId, String content);
    void modifyComment(Long articleId, Integer commentIdx, String content);
    void deleteComment(Long articleId, Integer commentIdx);
    void enterReply(Long articleId, Integer commentIdx, String content);
    void modifyReply(Long articleId, Integer commentIdx, Integer replyIdx, String content);
    void deleteReply(Long articleId, Integer commentIdx, Integer replyIdx);
}
