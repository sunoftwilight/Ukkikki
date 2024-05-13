package project.domain.article.service;

import project.domain.article.collection.CommentCollection;
import project.domain.article.dto.response.ArticleCreateResDto;

public interface CommentService {
    // 댓글 관리
    void createComment(ArticleCreateResDto articleCreateResDto);
    CommentCollection articleComment(Long articleId);
    void enterComment(Long articleId, String content);
    void modifyComment(Long articleId, Long commentIdx, String content);
    void deleteComment(Long articleId, Long commentIdx);
}
